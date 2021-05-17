import { UserRefreshToken } from "../entity/UserRefreshToken";
import { getManager, getRepository, InsertResult } from "typeorm";
import { User } from "../entity/User";

export async function createUser(
  username: string,
  nativeLanguageId: string,
  email?: string,
  password?: string,
  avatar?: string
) {
  const userRepo = getRepository(User);

  console.log(nativeLanguageId);

  await userRepo.insert({
    username,
    email,
    password,
    nativeLanguageId,
    avatar,
  });
}

export async function getOneUser(usernameOrEmail: string) {
  const userRepo = getRepository(User);

  const user = await userRepo
    .createQueryBuilder("user")
    .where("user.username = :username", { username: usernameOrEmail })
    .orWhere("user.email = :email", { email: usernameOrEmail })
    .getOne();

  return user;
}

export async function saveRefreshToken(userId: string, refreshToken: string) {
  const userTokenRepo = getRepository(UserRefreshToken);

  await userTokenRepo.insert({ userId, token: refreshToken });
}

export async function findOneRefreshToken(token: string) {
  const userTokenRepo = getRepository(UserRefreshToken);

  return await userTokenRepo.findOne({ token });
}
