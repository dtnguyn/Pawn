import { UserRefreshToken } from "../entity/UserRefreshToken";
import { getManager, getRepository, InsertResult } from "typeorm";
import { User } from "../entity/User";
import { VerificationCode } from "../entity/VerificationCode";
import nodemailer from "nodemailer";

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
  if (await verifyRepo.findOne({ email, code })) {
    await verifyRepo.delete({ email });
    return true;
  } else {
    return false;
  }
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

    let testAccount = await nodemailer.createTestAccount();

    const transporter = nodemailer.createTransport({
      host: "smtp.ethereal.email",
      port: 587,
      secure: false, // true for 465, false for other ports
      auth: {
        user: testAccount.user, // generated ethereal user
        pass: testAccount.pass, // generated ethereal password
      },
    });

    let info = await transporter.sendMail({
      from: testAccount.user, // sender address
      to: email, // list of receivers
      subject: "Verification code", // Subject line
      text: `Please use this code to verify your account: ${code}`, // plain text body
    });

    console.log("Message sent to: ", email);
    // Message sent: <b658f8ca-6296-ccf4-8306-87d57a0b4321@example.com>

    // Preview only available when sending through an Ethereal account
    console.log("Preview URL: %s", nodemailer.getTestMessageUrl(info));
    // Preview URL: https://ethereal.email/message/WaQKMgKddxQDoou...

    return code;
  } else {
    throw new Error("Please provide a valid email!");
  }
};

export const changePassword = async (
  email: string,
  code: string,
  hashPW: string
) => {
  if (await verifyCode(email, code)) {
    const userRepo = getRepository(User);
    await userRepo.update({ email }, { password: hashPW });
  } else throw new Error("Invalid verification code!");
};
