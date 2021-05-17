import dotenv from "dotenv";
dotenv.config();
import { Request, Response, Router } from "express";
import {
  createUser,
  findOneRefreshToken,
  getOneUser,
  saveRefreshToken,
} from "../controllers/UserController";
import jwt from "jsonwebtoken";
import bcrypt from "bcrypt";
import express from "express";

const router = Router();
router.use(express.json());

//Middlewares
const checkAuthentication = (req: Request, res: Response, next: Function) => {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];
  if (token == null) return res.sendStatus(401);

  jwt.verify(
    token,
    process.env.ACCESS_TOKEN_SECRET!,
    (err: any, decoded: any) => {
      console.log(err);
      if (err) {
        res.sendStatus(403);
      } else {
        (req as any).user = decoded.user;
        next();
      }
    }
  );
};

router.get("/", checkAuthentication, (req, res) => {
  const user = req && (req as any).user;
  if (user) {
    console.log(user);
    res.json(user);
  } else {
    res.sendStatus(404);
  }
});

//Routes
router.post("/register", async (req, res) => {
  try {
    const username = req.body.username;
    const email = req.body.email;
    const password = req.body.password;
    const avatar = req.body.avatar;
    const native = req.body.nativeLanguage;

    console.log(req.body);
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
      expiresIn: "30s",
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

router.post("/token", (req, res) => {
  const refreshToken = req.body.token;
  if (refreshToken == null) return res.sendStatus(401);
  if (!findOneRefreshToken(refreshToken)) return res.sendStatus(403);
  jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET!, (err, user) => {
    if (err) return res.sendStatus(403);
    else {
      const accessToken = jwt.sign({ user }, process.env.ACCESS_TOKEN_SECRET!, {
        expiresIn: "30s",
      });
      res.json({ accessToken: accessToken });
    }
  });
});

export default router;
