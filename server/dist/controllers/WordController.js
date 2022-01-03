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
const all_the_german_words_1 = __importDefault(require("all-the-german-words"));
const an_array_of_english_words_1 = __importDefault(require("an-array-of-english-words"));
const an_array_of_french_words_1 = __importDefault(require("an-array-of-french-words"));
const an_array_of_spanish_words_1 = __importDefault(require("an-array-of-spanish-words"));
const CustomError_1 = __importDefault(require("../utils/CustomError"));
const superagent_1 = __importDefault(require("superagent"));
const typeorm_1 = require("typeorm");
const Definition_1 = require("../entity/Definition");
const Language_1 = require("../entity/Language");
const Pronunciation_1 = require("../entity/Pronunciation");
const SavedWord_1 = require("../entity/SavedWord");
const Word_1 = require("../entity/Word");
const getRandomNumber_1 = require("../utils/getRandomNumber");
const enWordTopics_1 = require("../utils/enWordTopics");
const esWordTopics_1 = require("../utils/esWordTopics");
const frWordTopics_1 = require("../utils/frWordTopics");
const deWordTopics_1 = require("../utils/deWordTopics");
exports.getDailyRandomWords = (count, topic, language) => __awaiter(this, void 0, void 0, function* () {
    const results = [];
    let wordCount = count;
    let allWords = [];
    if (topic === "random") {
        switch (language) {
            case "en_US": {
                allWords = an_array_of_english_words_1.default;
                break;
            }
            case "de": {
                allWords = all_the_german_words_1.default;
                break;
            }
            case "fr": {
                allWords = an_array_of_french_words_1.default;
                break;
            }
            case "es": {
                allWords = an_array_of_spanish_words_1.default;
                break;
            }
            default: {
                allWords = [];
            }
        }
    }
    else {
        allWords = (yield typeorm_1.getRepository(Word_1.Word)
            .createQueryBuilder("word")
            .where("LOWER(word.topics) LIKE LOWER(:topic)", { topic: `%${topic}%` })
            .andWhere("word.language = :language", { language })
            .getMany()).map((word) => word.value);
    }
    console.log("all words results: ", allWords);
    const arr = [];
    while (results.length < wordCount) {
        let i = 0;
        const promises = [];
        while (i < wordCount) {
            const num = getRandomNumber_1.getRandomNumber(0, allWords.length);
            const word = allWords[num];
            if (!arr.includes(word)) {
                arr.push(word);
                promises.push(exports.getWordDetailSimplify(word, language));
                i++;
            }
        }
        yield Promise.all(promises)
            .then((details) => {
            details.forEach((detail) => {
                if (detail != null) {
                    results.push(detail);
                    wordCount--;
                }
            });
        })
            .catch((error) => {
            console.log("Error getting daily word: ", error.message);
            throw new CustomError_1.default("Something went wrong!");
        });
    }
    return results;
});
exports.getWordDetail = (wordString, language) => __awaiter(this, void 0, void 0, function* () {
    const res = yield superagent_1.default
        .get(`https://api.dictionaryapi.dev/api/v2/entries/${language}/${wordString}`)
        .catch(() => {
        return null;
    });
    if (res && res.body && res.body[0]) {
        const detail = res.body[0];
        const wordInDb = yield typeorm_1.getRepository(Word_1.Word).findOne({
            value: wordString,
            language,
        });
        const topics = wordInDb ? wordInDb.topics : "unknown";
        const word = {
            value: detail.word,
            language,
            topics,
            pronunciations: [],
            definitions: [],
        };
        if (detail.phonetics.length) {
            for (const phonetic of detail.phonetics) {
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
        if (detail.meanings.length) {
            for (const meaning of detail.meanings) {
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
exports.getWordDetailSimplify = (wordString, language) => __awaiter(this, void 0, void 0, function* () {
    const detail = yield exports.getWordDetail(wordString, language);
    const wordInDb = yield typeorm_1.getRepository(Word_1.Word).findOne({
        value: wordString,
        language,
    });
    const topics = wordInDb ? wordInDb.topics : "unknown";
    if (detail && detail.definitions.length) {
        const word = {
            value: detail.value,
            topics,
            language: detail.language,
            mainDefinition: detail.definitions[0].meaning,
            pronunciationSymbol: detail.pronunciations.length
                ? detail.pronunciations[0].symbol
                : null,
            pronunciationAudio: detail.pronunciations.length
                ? detail.pronunciations[0].audio
                : null,
        };
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
    return autoCompletes.map((word) => {
        return {
            value: word.value,
            language,
            topics: word.topics,
            mainDefinition: "",
            pronunciationSymbol: null,
            pronunciationAudio: null,
        };
    });
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
        const detail = yield exports.getWordDetail(word, language);
        if (detail) {
            const queryRunner = typeorm_1.getConnection().createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const manager = queryRunner.manager;
                const length = yield savedWordRepo.count({ userId, language });
                const wordInDb = yield typeorm_1.getRepository(Word_1.Word).findOne({
                    value: word,
                    language,
                });
                const topics = wordInDb ? wordInDb.topics : "unknown";
                const savedWordDb = manager.create(SavedWord_1.SavedWord, {
                    userId,
                    value: word,
                    language,
                    topics,
                    position: length + 1,
                });
                yield manager.save(SavedWord_1.SavedWord, savedWordDb);
                for (const pron of detail.pronunciations) {
                    yield manager.insert(Pronunciation_1.Pronunciation, {
                        savedWordId: savedWordDb.id,
                        symbol: pron.symbol,
                        audio: pron.audio,
                    });
                }
                if (detail.definitions.length == 0)
                    throw new CustomError_1.default("No definition found!");
                for (let i = 0; i < detail.definitions.length; i++) {
                    const definition = detail.definitions[i];
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
            throw new CustomError_1.default("Not found definition");
    }
});
exports.getSavedWords = (userId, language) => __awaiter(this, void 0, void 0, function* () {
    const savedWordRepo = typeorm_1.getRepository(SavedWord_1.SavedWord);
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
    const simplifySavedWords = savedWords.map((word) => {
        return {
            value: word.value,
            language: word.language,
            mainDefinition: word.definitions[0].meaning,
            pronunciationSymbol: word.pronunciations.length
                ? word.pronunciations[0].symbol
                : null,
            pronunciationAudio: word.pronunciations.length
                ? word.pronunciations[0].audio
                : null,
            topics: word.topics,
        };
    });
    return simplifySavedWords;
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
const getEnWordTopicMap = () => {
    const map = {};
    for (const word of enWordTopics_1.enCommon) {
        map[word.toLowerCase()] = "common";
    }
    for (const word of enWordTopics_1.enSport) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", sport";
        }
        else {
            map[word.toLowerCase()] = "sport";
        }
    }
    for (const word of enWordTopics_1.enNature) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", nature";
        }
        else {
            map[word.toLowerCase()] = "nature";
        }
    }
    for (const word of enWordTopics_1.enClothing) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", clothing";
        }
        else {
            map[word.toLowerCase()] = "clothing";
        }
    }
    for (const word of enWordTopics_1.enEmotion) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", emotion";
        }
        else {
            map[word.toLowerCase()] = "emotion";
        }
    }
    for (const word of enWordTopics_1.enFood) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", food";
        }
        else {
            map[word.toLowerCase()] = "food";
        }
    }
    for (const word of enWordTopics_1.enMoney) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", money";
        }
        else {
            map[word.toLowerCase()] = "money";
        }
    }
    for (const word of enWordTopics_1.enMovie) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", movie";
        }
        else {
            map[word.toLowerCase()] = "movie";
        }
    }
    for (const word of enWordTopics_1.enMusic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] = ", music";
        }
        else {
            map[word.toLowerCase()] = "music";
        }
    }
    for (const word of enWordTopics_1.enScience) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", science";
        }
        else {
            map[word.toLowerCase()] = "science";
        }
    }
    for (const word of enWordTopics_1.enWeather) {
        if (map[word]) {
            map[word.toLowerCase()] += ", weather";
        }
        else {
            map[word.toLowerCase()] = "weather";
        }
    }
    for (const word of enWordTopics_1.enTraffic) {
        if (map[word]) {
            map[word.toLowerCase()] += ", traffic";
        }
        else {
            map[word.toLowerCase()] = "traffic";
        }
    }
    return map;
};
const getEsWordTopicMap = () => {
    const map = {};
    for (const word of esWordTopics_1.esCommon) {
        map[word.toLowerCase()] = "common";
    }
    for (const word of esWordTopics_1.esSport) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", sport";
        }
        else {
            map[word.toLowerCase()] = "sport";
        }
    }
    for (const word of esWordTopics_1.esNature) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", nature";
        }
        else {
            map[word.toLowerCase()] = "nature";
        }
    }
    for (const word of esWordTopics_1.esClothing) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", clothing";
        }
        else {
            map[word.toLowerCase()] = "clothing";
        }
    }
    for (const word of esWordTopics_1.esEmotion) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", emotion";
        }
        else {
            map[word.toLowerCase()] = "emotion";
        }
    }
    for (const word of esWordTopics_1.esFood) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", food";
        }
        else {
            map[word.toLowerCase()] = "food";
        }
    }
    for (const word of esWordTopics_1.esMoney) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", money";
        }
        else {
            map[word.toLowerCase()] = "money";
        }
    }
    for (const word of esWordTopics_1.esMovie) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", movie";
        }
        else {
            map[word.toLowerCase()] = "movie";
        }
    }
    for (const word of esWordTopics_1.esMusic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] = ", music";
        }
        else {
            map[word.toLowerCase()] = "music";
        }
    }
    for (const word of esWordTopics_1.esScience) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", science";
        }
        else {
            map[word.toLowerCase()] = "science";
        }
    }
    for (const word of esWordTopics_1.esWeather) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", weather";
        }
        else {
            map[word.toLowerCase()] = "weather";
        }
    }
    for (const word of esWordTopics_1.esTraffic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", traffic";
        }
        else {
            map[word.toLowerCase()] = "traffic";
        }
    }
    return map;
};
const getFrWordTopicMap = () => {
    const map = {};
    for (const word of frWordTopics_1.frCommon) {
        map[word.toLowerCase()] = "common";
    }
    for (const word of frWordTopics_1.frSport) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", sport";
        }
        else {
            map[word.toLowerCase()] = "sport";
        }
    }
    for (const word of frWordTopics_1.frNature) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", nature";
        }
        else {
            map[word.toLowerCase()] = "nature";
        }
    }
    for (const word of frWordTopics_1.frClothing) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", clothing";
        }
        else {
            map[word.toLowerCase()] = "clothing";
        }
    }
    for (const word of frWordTopics_1.frEmotion) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", emotion";
        }
        else {
            map[word.toLowerCase()] = "emotion";
        }
    }
    for (const word of frWordTopics_1.frFood) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", food";
        }
        else {
            map[word.toLowerCase()] = "food";
        }
    }
    for (const word of frWordTopics_1.frMoney) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", money";
        }
        else {
            map[word.toLowerCase()] = "money";
        }
    }
    for (const word of frWordTopics_1.frMovie) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", movie";
        }
        else {
            map[word.toLowerCase()] = "movie";
        }
    }
    for (const word of frWordTopics_1.frMusic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] = ", music";
        }
        else {
            map[word.toLowerCase()] = "music";
        }
    }
    for (const word of frWordTopics_1.frScience) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", science";
        }
        else {
            map[word.toLowerCase()] = "science";
        }
    }
    for (const word of frWordTopics_1.frWeather) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", weather";
        }
        else {
            map[word.toLowerCase()] = "weather";
        }
    }
    for (const word of frWordTopics_1.frTraffic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", traffic";
        }
        else {
            map[word.toLowerCase()] = "traffic";
        }
    }
    return map;
};
const getDeWordTopicMap = () => {
    const map = {};
    for (const word of deWordTopics_1.deCommon) {
        map[word.toLowerCase()] = "common";
    }
    for (const word of deWordTopics_1.deSport) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", sport";
        }
        else {
            map[word.toLowerCase()] = "sport";
        }
    }
    for (const word of deWordTopics_1.deNature) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", nature";
        }
        else {
            map[word.toLowerCase()] = "nature";
        }
    }
    for (const word of deWordTopics_1.deClothing) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", clothing";
        }
        else {
            map[word.toLowerCase()] = "clothing";
        }
    }
    for (const word of deWordTopics_1.deEmotion) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", emotion";
        }
        else {
            map[word.toLowerCase()] = "emotion";
        }
    }
    for (const word of deWordTopics_1.deFood) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", food";
        }
        else {
            map[word.toLowerCase()] = "food";
        }
    }
    for (const word of deWordTopics_1.deMoney) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", money";
        }
        else {
            map[word.toLowerCase()] = "money";
        }
    }
    for (const word of deWordTopics_1.deMovie) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", movie";
        }
        else {
            map[word.toLowerCase()] = "movie";
        }
    }
    for (const word of deWordTopics_1.deMusic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] = ", music";
        }
        else {
            map[word.toLowerCase()] = "music";
        }
    }
    for (const word of deWordTopics_1.deScience) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", science";
        }
        else {
            map[word.toLowerCase()] = "science";
        }
    }
    for (const word of deWordTopics_1.deWeather) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", weather";
        }
        else {
            map[word.toLowerCase()] = "weather";
        }
    }
    for (const word of deWordTopics_1.deTraffic) {
        if (map[word.toLowerCase()]) {
            map[word.toLowerCase()] += ", traffic";
        }
        else {
            map[word.toLowerCase()] = "traffic";
        }
    }
    return map;
};
const importEnWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const map = getEnWordTopicMap();
    for (const word of an_array_of_english_words_1.default) {
        const topics = map[word] && typeof map[word] == "string"
            ? map[word]
            : "unknown";
        yield wordRepo.insert({
            value: word,
            language: "en_US",
            topics,
        });
    }
});
const importDeWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const map = getDeWordTopicMap();
    for (const word of all_the_german_words_1.default) {
        const topics = map[word] && typeof map[word] == "string"
            ? map[word]
            : "unknown";
        yield wordRepo.insert({
            value: word,
            language: "de",
            topics,
        });
    }
});
const importFrWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const map = getFrWordTopicMap();
    for (const word of an_array_of_french_words_1.default) {
        const topics = map[word] && typeof map[word] == "string"
            ? map[word]
            : "unknown";
        yield wordRepo.insert({
            value: word,
            language: "fr",
            topics,
        });
    }
});
const importEsWords = () => __awaiter(this, void 0, void 0, function* () {
    const wordRepo = typeorm_1.getRepository(Word_1.Word);
    const map = getEsWordTopicMap();
    for (const word of an_array_of_spanish_words_1.default) {
        const topics = map[word] && typeof map[word] == "string"
            ? map[word]
            : "unknown";
        yield wordRepo.insert({
            value: word,
            language: "es",
            topics,
        });
    }
});
//# sourceMappingURL=WordController.js.map