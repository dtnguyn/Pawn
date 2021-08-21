import { Language } from "../entity/Language";
import { User } from "../entity/User";
import { getRepository } from "typeorm";
import CustomError from "../utils/CustomError";

export const chooseLanguages = async (
  languageSymbols: string[],
  userId: string
) => {
  const userRepo = getRepository(User);
  const languageRepo = getRepository(Language);
  const user = await userRepo
    .createQueryBuilder("user")
    .leftJoinAndSelect("user.learningLanguages", "language")
    .where("user.id = :userId", { userId })
    .getOne();

  if (!user) throw new CustomError("User not found!");
  console.log(user);
  user.learningLanguages = [];
  for (const languageSymbol of languageSymbols) {
    const language = await languageRepo.findOne({ id: languageSymbol });
    if (language) {
      user.learningLanguages.push(language);
      await userRepo.save(user);
    }
  }
  console.log(
    await userRepo
      .createQueryBuilder("user")
      .leftJoinAndSelect("user.learningLanguages", "language")
      .where("user.id = :userId", { userId })
      .getOne()
  );
};

export const getLearningLanguages = async (userId: string) => {
  const userRepo = getRepository(User);
  const user = await userRepo
    .createQueryBuilder("user")
    .leftJoinAndSelect("user.learningLanguages", "language")
    .where("user.id = :userId", { userId })
    .getOne();

  if (!user) return [];

  return user.learningLanguages;
};
