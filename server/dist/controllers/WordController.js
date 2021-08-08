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
const an_array_of_english_words_1 = __importDefault(require("an-array-of-english-words"));
const an_array_of_french_words_1 = __importDefault(require("an-array-of-french-words"));
const an_array_of_spanish_words_1 = __importDefault(require("an-array-of-spanish-words"));
const all_the_german_words_1 = __importDefault(require("all-the-german-words"));
const getRandomNumber_1 = require("../utils/getRandomNumber");
const superagent_1 = __importDefault(require("superagent"));
const typeorm_1 = require("typeorm");
const Word_1 = require("../entity/Word");
const SavedWord_1 = require("../entity/SavedWord");
const Pronunciation_1 = require("../entity/Pronunciation");
const Definition_1 = require("../entity/Definition");
const Language_1 = require("../entity/Language");
exports.getDailyRandomWords = (wordCount, language) => __awaiter(this, void 0, void 0, function* () {
    const results = [];
    switch (language) {
        case "en_US": {
            let i = 0;
            const arr = [];
            while (i < wordCount) {
                const num = getRandomNumber_1.getRandomNumber(0, 274936);
                const word = an_array_of_english_words_1.default[num];
                if (!arr.includes(word)) {
                    const def = yield exports.getDefinition(word, language);
                    if (def) {
                        results.push(def);
                        arr.push(word);
                        i++;
                    }
                }
            }
            console.log("tries: ", i);
            break;
        }
        case "de": {
            let i = 0;
            const arr = [];
            while (i < wordCount) {
                const num = getRandomNumber_1.getRandomNumber(0, 1680837);
                const word = all_the_german_words_1.default[num];
                if (!arr.includes(word)) {
                    const def = yield exports.getDefinition(word, language);
                    if (def) {
                        results.push(def);
                        arr.push(word);
                        i++;
                    }
                }
            }
            break;
        }
        case "fr": {
            let i = 0;
            const arr = [];
            while (i < wordCount) {
                const num = getRandomNumber_1.getRandomNumber(0, 336523);
                const word = an_array_of_french_words_1.default[num];
                if (!arr.includes(word)) {
                    const def = yield exports.getDefinition(word, language);
                    if (def) {
                        results.push(def);
                        arr.push(word);
                        i++;
                    }
                }
            }
            break;
        }
        case "es": {
            let i = 0;
            const arr = [];
            while (i < wordCount) {
                const num = getRandomNumber_1.getRandomNumber(0, 636597);
                const word = an_array_of_spanish_words_1.default[num];
                if (!arr.includes(word)) {
                    const def = yield exports.getDefinition(word, language);
                    if (def) {
                        results.push(def);
                        arr.push(word);
                        i++;
                    }
                }
            }
            break;
        }
        default: {
            throw new Error("Please provide supported language!");
        }
    }
    return results;
});
exports.getDefinition = (word, language) => __awaiter(this, void 0, void 0, function* () {
    const res = yield superagent_1.default
        .get(`https://api.dictionaryapi.dev/api/v2/entries/${language}/${word}`)
        .catch(() => {
        return null;
    });
    if (res && res.body && res.body[0]) {
        const def = res.body[0];
        const word = {
            value: def.word,
            language,
            pronunciations: [],
            definitions: [],
        };
        if (def.phonetics.length) {
            for (const phonetic of def.phonetics) {
                if (phonetic.audio && phonetic.text) {
                    const pron = {
                        audio: phonetic.audio,
                        symbol: phonetic.text,
                    };
                    word.pronunciations.push(pron);
                }
            }
        }
        else
            return null;
        if (def.meanings.length) {
            for (const meaning of def.meanings) {
                for (const definition of meaning.definitions) {
                    if (definition.definition) {
                        const defValue = {
                            meaning: definition.definition,
                            partOfSpeech: meaning.partOfSpeech,
                            example: definition.example,
                        };
                        word.definitions.push(defValue);
                    }
                }
            }
        }
        else
            return null;
        return word;
    }
    else
        return null;
});
exports.getWordAutoCompletes = (language, text) => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const autoCompletes = yield wordRepo
        .createQueryBuilder("word")
        .where("LOWER(word.value) LIKE LOWER(:text)", { text: `${text}%` })
        .andWhere("word.language = :language", { language })
        .limit(10)
        .getMany();
    return autoCompletes;
});
exports.toggleSaveWord = (word, language, userId) => __awaiter(this, void 0, void 0, function* () {
    const savedWordRepo = typeorm_1.getRepository(SavedWord_1.SavedWord);
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const savedWord = yield savedWordRepo.findOne({
        value: word,
        language,
        userId,
    });
    if (savedWord) {
        yield savedWordRepo.delete({
            id: savedWord.id,
        });
    }
    else {
        const def = yield exports.getDefinition(word, language);
        console.log("Definition: ", def);
        if (def) {
            const queryRunner = typeorm_1.getConnection().createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const manager = queryRunner.manager;
                const length = yield savedWordRepo.count({ userId, language });
                const savedWordDb = manager.create(SavedWord_1.SavedWord, {
                    userId,
                    value: word,
                    language,
                    position: length + 1,
                });
                yield manager.save(SavedWord_1.SavedWord, savedWordDb);
                for (const pron of def.pronunciations) {
                    yield manager.insert(Pronunciation_1.Pronunciation, {
                        savedWordId: savedWordDb.id,
                        symbol: pron.symbol,
                        audio: pron.audio,
                    });
                }
                for (let i = 0; i < def.definitions.length; i++) {
                    const definition = def.definitions[i];
                    yield manager.insert(Definition_1.Definition, {
                        savedWordId: savedWordDb.id,
                        meaning: definition.meaning,
                        example: definition.example,
                        partOfSpeech: definition.partOfSpeech,
                        position: i + 1,
                    });
                }
                yield queryRunner.commitTransaction();
                yield queryRunner.release();
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                throw error;
            }
        }
        else
            throw new Error("Not found definition");
    }
});
exports.getSavedWords = (userId, language) => __awaiter(this, void 0, void 0, function* () {
    const savedWordRepo = typeorm_1.getRepository(SavedWord_1.SavedWord);
    console.log("Getting saved words... ");
    const savedWords = yield savedWordRepo
        .createQueryBuilder("savedWord")
        .leftJoinAndSelect("savedWord.pronunciations", "pronunciations")
        .leftJoinAndSelect("savedWord.definitions", "definitions")
        .orderBy("definitions.position", "ASC")
        .leftJoinAndSelect("savedWord.user", "user")
        .where("user.id = :userId", { userId })
        .andWhere("savedWord.language = :language", { language })
        .orderBy("savedWord.position", "ASC")
        .getMany();
    console.log(savedWords);
    return savedWords;
});
exports.rearrangeSavedWords = (wordIds) => __awaiter(this, void 0, void 0, function* () {
    const savedWordRepo = typeorm_1.getRepository(SavedWord_1.SavedWord);
    yield new Promise((resolve, reject) => {
        let counter = 0;
        for (let i = 0; i < wordIds.length; i++) {
            savedWordRepo
                .update({ id: wordIds[i] }, { position: i + 1 })
                .then(() => {
                counter += 1;
                if (counter === wordIds.length) {
                    resolve(true);
                }
            })
                .catch((e) => reject(e));
        }
    });
});
exports.rearrangeDefinition = (definitionIds) => __awaiter(this, void 0, void 0, function* () {
    const definitionRepo = typeorm_1.getRepository(Definition_1.Definition);
    yield new Promise((resolve, reject) => {
        let counter = 0;
        for (let i = 0; i < definitionIds.length; i++) {
            definitionRepo
                .update({ id: definitionIds[i] }, { position: i + 1 })
                .then(() => {
                counter += 1;
                if (counter === definitionIds.length) {
                    resolve(true);
                }
            })
                .catch((e) => reject(e));
        }
    });
});
exports.importAllLanguages = () => __awaiter(this, void 0, void 0, function* () {
    const languageRepo = typeorm_1.getRepository(Language_1.Language);
    if (!(yield languageRepo.findOne({ id: "en_US" })))
        yield languageRepo.insert({
            id: "en_US",
            value: "English",
        });
    if (!(yield languageRepo.findOne({ id: "fr" })))
        yield languageRepo.insert({
            id: "fr",
            value: "French",
        });
    if (!(yield languageRepo.findOne({ id: "es" })))
        yield languageRepo.insert({
            id: "es",
            value: "Spanish",
        });
    if (!(yield languageRepo.findOne({ id: "de" })))
        yield languageRepo.insert({
            id: "de",
            value: "German",
        });
});
exports.importAllWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const enArr = yield wordRepo.find({ language: "en_US" });
    if (!enArr.length)
        yield importEnWords();
    console.log("Finish import all English words");
    const frArr = yield wordRepo.find({ language: "fr" });
    if (!frArr.length)
        yield importFrWords();
    console.log("Finish import all French words");
    const esArr = yield wordRepo.find({ language: "es" });
    if (!esArr.length)
        yield importEsWords();
    console.log("Finish import all Spanish words");
    const deArr = yield wordRepo.find({ language: "de" });
    if (!deArr.length)
        yield importDeWords();
    console.log("Finish import all German words");
});
const importEnWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    for (const word of an_array_of_english_words_1.default) {
        yield wordRepo.insert({
            value: word,
            language: "en_US",
        });
    }
});
const importDeWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    for (const word of all_the_german_words_1.default) {
        yield wordRepo.insert({
            value: word,
            language: "de",
        });
    }
});
const importFrWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    for (const word of an_array_of_french_words_1.default) {
        yield wordRepo.insert({
            value: word,
            language: "fr",
        });
    }
});
const importEsWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    for (const word of an_array_of_spanish_words_1.default) {
        yield wordRepo.insert({
            value: word,
            language: "es",
        });
    }
});
//# sourceMappingURL=WordController.js.map