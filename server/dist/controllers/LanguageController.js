"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const Language_1 = require("../entity/Language");
const User_1 = require("../entity/User");
const typeorm_1 = require("typeorm");
exports.chooseLanguages = (languageSymbols, userId) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    const languageRepo = typeorm_1.getRepository(Language_1.Language);
    const user = yield userRepo
        .createQueryBuilder("user")
        .leftJoinAndSelect("user.learningLanguages", "language")
        .where("user.id = :userId", { userId })
        .getOne();
    if (!user)
        throw new Error("User not found!");
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
//# sourceMappingURL=LanguageController.js.map