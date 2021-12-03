"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const Language_1 = require("../entity/Language");
const User_1 = require("../entity/User");
const typeorm_1 = require("typeorm");
const CustomError_1 = __importDefault(require("../utils/CustomError"));
const SavedWord_1 = require("../entity/SavedWord");
const constants_1 = require("../utils/constants");
exports.chooseLanguages = (languageSymbols, userId) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    const languageRepo = typeorm_1.getRepository(Language_1.Language);
    const user = yield userRepo
        .createQueryBuilder("user")
        .leftJoinAndSelect("user.learningLanguages", "language")
        .where("user.id = :userId", { userId })
        .getOne();
    if (!user)
        throw new CustomError_1.default("User not found!");
    console.log(user);
    user.learningLanguages = [];
    for (const languageSymbol of languageSymbols) {
        const language = yield languageRepo.findOne({ id: languageSymbol });
        if (language) {
            user.learningLanguages.push(language);
            yield userRepo.save(user);
        }
    }
    console.log(yield userRepo
        .createQueryBuilder("user")
        .leftJoinAndSelect("user.learningLanguages", "language")
        .where("user.id = :userId", { userId })
        .getOne());
});
exports.getLearningLanguages = (userId) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    const user = yield userRepo
        .createQueryBuilder("user")
        .leftJoinAndSelect("user.learningLanguages", "language")
        .where("user.id = :userId", { userId })
        .getOne();
    if (!user)
        return [];
    return user.learningLanguages;
});
exports.getLanguageReports = (userId) => __awaiter(this, void 0, void 0, function* () {
    const languageRepo = typeorm_1.getRepository(Language_1.Language);
    const languages = yield languageRepo
        .createQueryBuilder("language")
        .leftJoinAndSelect("language.learners", "user")
        .where("user.id = :userId", { userId })
        .getMany();
    const languageReports = yield Promise.all(languages.map((language) => {
        return new Promise((resolve, reject) => {
            getLanguageReport(userId, language.id)
                .then((report) => {
                resolve(report);
            })
                .catch((error) => {
                reject(error);
            });
        });
    }));
    return languageReports;
});
const getLanguageReport = (userId, languageId) => __awaiter(this, void 0, void 0, function* () {
    const savedWordRepo = typeorm_1.getRepository(SavedWord_1.SavedWord);
    const savedWordCount = yield savedWordRepo.count({
        userId,
        language: languageId,
    });
    const wordTopicReports = yield Promise.all(constants_1.supportedWordTopics.map((topic) => {
        return new Promise((resolve, reject) => __awaiter(this, void 0, void 0, function* () {
            savedWordRepo
                .createQueryBuilder("savedWord")
                .where("savedWord.userId = :userId", { userId })
                .andWhere("savedWord.language = :languageId", { languageId })
                .andWhere("savedWord.topics LIKE :topic", {
                topic: `%${topic.toLowerCase()}%`,
            })
                .getCount()
                .then((wordCount) => {
                const topicReport = {
                    languageId,
                    value: topic,
                    wordCount,
                };
                resolve(topicReport);
            })
                .catch((error) => {
                reject(error);
            });
        }));
    }));
    const languageReport = {
        languageId,
        savedWordCount,
        wordTopicReports,
    };
    return languageReport;
});
//# sourceMappingURL=LanguageController.js.map