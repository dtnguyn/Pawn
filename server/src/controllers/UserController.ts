import { UserRefreshToken } from "../entity/UserRefreshToken";
import { getManager, getRepository, InsertResult } from "typeorm";
import { User } from "../entity/User";
import { VerificationCode } from "../entity/VerificationCode";
import nodemailer from "nodemailer";
import CustomError from "../utils/CustomError";
import dotenv from "dotenv";
dotenv.config();

export async function createUser(
  username: string,
  nativeLanguageId: string,
  email?: string,
  password?: string,
  avatar?: string
) {
  const userRepo = getRepository(User);

  await userRepo.insert({
    username,
    email,
    password,
    nativeLanguageId,
    appLanguageId: nativeLanguageId,
    avatar,
  });
}

export async function createOauthUser(
  oauthId: string,
  username: string,
  email?: string,
  avatar?: string
) {
  const userRepo = getRepository(User);

  await userRepo.insert({
    oauthId,
    email,
    username,
    avatar,
  });
}

export async function getOneUser(usernameOrEmail: string) {
  const userRepo = getRepository(User);
  return await userRepo
    .createQueryBuilder("user")
    .leftJoinAndSelect("user.learningLanguages", "learningLanguages")
    .where("user.username = :username", { username: usernameOrEmail })
    .orWhere("user.email = :email", { email: usernameOrEmail })
    .getOne();
}

export async function getOneOauthUser(oauthId: string) {
  const userRepo = getRepository(User);

  return await userRepo.findOne({ oauthId });
}

export async function saveRefreshToken(userId: string, refreshToken: string) {
  const userTokenRepo = getRepository(UserRefreshToken);

  await userTokenRepo.insert({ userId, token: refreshToken });
}

export async function findOneRefreshToken(token: string) {
  const userTokenRepo = getRepository(UserRefreshToken);

  return await userTokenRepo.findOne({ token });
}

export const verifyCode = async (email: string, code: string) => {
  const verifyRepo = getRepository(VerificationCode);
  let result = false
  if (await verifyRepo.findOne({ email, code })) {
    await verifyRepo.delete({ email });
    return true;
  } else {
    result = false
  }

  return result;
};

export const sendVerificationCode = async (email: string) => {
  const userRepo = getRepository(User);

  if (email && (await userRepo.findOne({ email }))) {
    // send mail with defined transport object
    const code = Math.floor(100000 + Math.random() * 900000).toString();

    const verifyRepo = getRepository(VerificationCode);

    if (await verifyRepo.findOne({ email })) {
      await verifyRepo.update({ email }, { code });
    } else {
      await verifyRepo.insert({ email, code });
    }

    const transporter = nodemailer.createTransport({
      host: "mail.privateemail.com",
      port: 587,
      secure: false, // true for 465, false for other ports
      auth: {
        user: process.env.POLYGLOT_EMAIL, // generated ethereal user
        pass:  process.env.POLYGLOT_EMAIL_PASSWORD, // generated ethereal password
      },
      tls: {
        // do not fail on invalid certs
        rejectUnauthorized: false,
      },    
    });

    await transporter.sendMail({
      from:  process.env.POLYGLOT_EMAIL, // sender address
      to: email, // list of receivers
      subject: "Verification code", // Subject line
      text: `Please use this code to verify your account: ${code}`, // plain text body
    });

    return code;
  } else {
    throw new CustomError("Please provide a valid email!");
  }
};

export const deleteRefreshToken = async (refreshToken: string) => {
  try {
    await getRepository(UserRefreshToken).delete({ token: refreshToken });
  } catch (error) {
    throw new CustomError("Unable to logout!");
  }
};

export const changePassword = async (
  email: string,
  hashPW: string
) => {
  try {
    const userRepo = getRepository(User);
    await userRepo.update({ email }, { password: hashPW });
  } catch(error) {
    throw new CustomError("Unable to reset password!")
  }
  
};

export const updateUser = async (
  userId: string,
  username: string,
  email: string,
  isPremium: boolean,
  avatar: string | undefined,
  dailyWordCount: number,
  notificationEnabled: boolean,
  nativeLanguageId: string,
  appLanguageId: string,
  dailyWordTopic: string,
  feedTopics: string
) => {
  const userRepo = getRepository(User);
  console.log("app: ", dailyWordTopic);
  await userRepo.update(
    { id: userId },
    {
      username,
      email,
      isPremium,
      avatar,
      dailyWordCount,
      notificationEnabled,
      nativeLanguageId,
      appLanguageId,
      dailyWordTopic,
      feedTopics,
    }
  );

  const newUser = await userRepo.findOne({ id: userId });
  return newUser;
};
