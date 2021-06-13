import dotenv from "dotenv";
dotenv.config();
import { Request, Response, Router } from "express";
import {
  changePassword,
  createOauthUser,
  createUser,
  findOneRefreshToken,
  getOneOauthUser,
  getOneUser,
  saveRefreshToken,
  sendVerificationCode,
  verifyCode,
} from "../controllers/UserController";
import jwt from "jsonwebtoken";
import bcrypt from "bcrypt";
import express from "express";
import passport from "passport";
import { Strategy as GoogleStrategy } from "passport-google-oauth20";
import { VerifyCallback } from "passport-google-oauth20";
import nodemailer from "nodemailer";
import { getRepository } from "typeorm";
import { User } from "../entity/User";
import { VerificationCode } from "../entity/VerificationCode";

const router = Router();
router.use(express.json());

const handleAuth = async (
  _accessToken: string,
  _refreshToken: string,
  profile: any,
  cb: VerifyCallback
) => {
  try {
    const { id, emails, displayName, photos } = profile;
    console.log(id, emails, displayName, photos, profile);
    let user = await getOneOauthUser(id);
    if (!user) {
      //Register
      await createOauthUser(
        id,
        displayName,
        emails && emails[0].value,
        photos && photos[0].value
      );
      user = await getOneOauthUser(id);
    }
    if (user) return cb(null, user);
    else throw new Error("There's something wrong");
  } catch (error) {
    return cb(error, undefined);
  }
};

router.use(passport.initialize());
passport.use(
  new GoogleStrategy(
    {
      clientID: process.env.GOOGLE_CLIENT_ID as string,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET as string,
      callbackURL: process.env.GOOGLE_CLIENT_SECRET as string,
    },
    handleAuth
  )
);

//Middlewares
export const checkAuthentication = (
  req: Request,
  res: Response,
  next: Function
) => {
  const authHeader = req.headers["authorization"];
  let token = authHeader && authHeader.split(" ")[1];

  if (!token) res.sendStatus(401);
  else {
    jwt.verify(
      token,
      process.env.ACCESS_TOKEN_SECRET!,
      (err: any, decoded: any) => {
        if (err) {
          res.sendStatus(403);
        } else {
          (req as any).user = decoded.user;
          next();
        }
      }
    );
  }
};

//Routes
router.get("/", checkAuthentication, (req, res) => {
  const user = req && (req as any).user;
  if (user) {
    res.json(user);
  } else {
    res.sendStatus(404);
  }
});

router.get("/verify/code", async (req, res) => {
  try {
    const email = req.body.email;

    const code = await sendVerificationCode(email);

    res.json(code);
  } catch (error) {
    res.status(400).send({
      message: error.message,
    });
  }
});

router.post("/verify/code", async (req, res) => {
  try {
    const email = req.body.email;
    const code = req.body.code;

    const result = await verifyCode(email, code);

    res.json(result);
  } catch (error) {
    res.status(400).send({
      message: error.message,
    });
  }
});

router.post("/register", async (req, res) => {
  try {
    const username = req.body.username;
    const email = req.body.email;
    const password = req.body.password;
    const avatar = req.body.avatar;
    const native = req.body.nativeLanguage;

    //Check user input
    if (!username || !email || !password || !native)
      throw new Error("Please provide the required information");

    //Create hash password
    const hashPW = bcrypt.hashSync(
      password,
      parseInt(process.env.SALT_ROUNDS!)
    );

    //Create new user
    await createUser(username, native, email, hashPW, avatar);

    return res.json(true);
  } catch (error) {
    return res.status(400).send({
      message: error.message,
    });
  }
});

router.post("/login", async (req, res) => {
  // Authenticate User
  try {
    //Check user input
    if (!req.body.usernameOrEmail)
      throw new Error("Please provide username or email!");
    if (!req.body.password) throw new Error("Please provide password");

    //Check if user is registered
    const user = await getOneUser(req.body.usernameOrEmail);
    if (!user)
      throw new Error("No user found with the given username or password!");

    //Check password
    const result = await bcrypt.compare(req.body.password, user.password);
    if (!result) throw new Error("Invalid credentials!");

    //Create new tokens and save to database
    const accessToken = jwt.sign({ user }, process.env.ACCESS_TOKEN_SECRET!, {
      expiresIn: process.env.TOKEN_EXPIRATION,
    });
    const refreshToken = jwt.sign({ user }, process.env.REFRESH_TOKEN_SECRET!);
    await saveRefreshToken(user.id, refreshToken);

    //Send response
    return res.json({ accessToken, refreshToken });
  } catch (error) {
    return res.status(400).send({
      message: error.message,
    });
  }
});

router.patch("/password", async (req, res) => {
  try {
    const email = req.body.email;
    const newPassword = req.body.newPassword;
    const code = req.body.code;
    const hashPW = bcrypt.hashSync(
      newPassword,
      parseInt(process.env.SALT_ROUNDS!)
    );
    await changePassword(email, code, hashPW);

    res.json(true);
  } catch (error) {
    res.status(400).send({
      message: error.message,
    });
  }
});

router.post("/token", (req, res) => {
  try {
    const refreshToken = req.body.token;

    //Verify user input
    if (refreshToken == null) throw new Error("Unauthorized");

    //Check if the refresh token is stored in the database
    if (!findOneRefreshToken(refreshToken)) throw new Error("Forbidden");

    // Verify the refresh token
    jwt.verify(
      refreshToken,
      process.env.REFRESH_TOKEN_SECRET!,
      (err: any, decoded: any) => {
        if (err) res.sendStatus(403);
        else {
          const accessToken = jwt.sign(
            { user: decoded.user },
            process.env.ACCESS_TOKEN_SECRET!,
            {
              expiresIn: process.env.TOKEN_EXPIRATION,
            }
          );
          res.json({ accessToken: accessToken });
        }
      }
    );
  } catch (error) {
    if (error.message === "Unauthorized") {
      res.sendStatus(401);
    } else if (error.message === "Forbidden") {
      res.sendStatus(403);
    } else {
      res.sendStatus(400);
    }
  }
});

router.get(
  "/google",
  passport.authenticate("google", { scope: ["profile", "email"] })
);

router.get(
  "/google/callback",
  passport.authenticate("google", { session: false }),
  async (req, res) => {
    //Create new tokens and save to database
    const accessToken = jwt.sign(
      { user: (req as any).user },
      process.env.ACCESS_TOKEN_SECRET!,
      {
        expiresIn: process.env.TOKEN_EXPIRATION,
      }
    );
    const refreshToken = jwt.sign(
      { user: (req as any).user },
      process.env.REFRESH_TOKEN_SECRET!
    );
    await saveRefreshToken((req as any).user.id, refreshToken);

    res.json({
      accessToken: accessToken,
      refreshToken: refreshToken,
    });
  }
);

export default router;
