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
                const pron = {
                    audio: phonetic.audio,
                    symbol: phonetic.text,
                };
                word.pronunciations.push(pron);
            }
        }
        else
            return null;
        if (def.meanings.length) {
            for (const meaning of def.meanings) {
                for (const definition of meaning.definitions) {
                    const defValue = {
                        meaning: definition.definition,
                        partOfSpeech: meaning.partOfSpeech,
                        example: definition.example,
                    };
                    word.definitions.push(defValue);
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
//# sourceMappingURL=WordController.js.map