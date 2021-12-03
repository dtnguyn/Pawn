import { Language } from "../entity/Language";
import { User } from "../entity/User";
import { getRepository } from "typeorm";
import CustomError from "../utils/CustomError";
import { SavedWord } from "../entity/SavedWord";
import { supportedWordTopics } from "../utils/constants";
import { LanguageReport, WordTopicReport } from "../utils/types";

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

export const getLanguageReports = async (userId: string) => {
  const languageRepo = getRepository(Language);
  const languages = await languageRepo
    .createQueryBuilder("language")
    .leftJoinAndSelect("language.learners", "user")
    .where("user.id = :userId", { userId })
    .getMany();

  const languageReports = await Promise.all(
    languages.map((language) => {
      return new Promise<LanguageReport>((resolve, reject) => {
        getLanguageReport(userId, language.id)
          .then((report) => {
            resolve(report);
          })
          .catch((error) => {
            reject(error);
          });
      });
    })
  );

  return languageReports;
};

const getLanguageReport = async (userId: string, languageId: string) => {
  const savedWordRepo = getRepository(SavedWord);

  const savedWordCount = await savedWordRepo.count({
    userId,
    language: languageId,
  });

  const wordTopicReports = await Promise.all(
    supportedWordTopics.map((topic) => {
      return new Promise<WordTopicReport>(async (resolve, reject) => {
        savedWordRepo
          .createQueryBuilder("savedWord")
          .where("savedWord.userId = :userId", { userId })
          .andWhere("savedWord.language = :languageId", { languageId })
          .andWhere("savedWord.topics LIKE :topic", {
            topic: `%${topic.toLowerCase()}%`,
          })
          .getCount()
          .then((wordCount) => {
            const topicReport: WordTopicReport = {
              languageId,
              value: topic,
              wordCount,
            };
            resolve(topicReport);
          })
          .catch((error) => {
            reject(error);
          });
      });
    })
  );

  const languageReport: LanguageReport = {
    languageId,
    savedWordCount,
    wordTopicReports,
  };

  return languageReport;
};
