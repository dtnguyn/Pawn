import deWords from "all-the-german-words";
import enWords from "an-array-of-english-words";
import frWords from "an-array-of-french-words";
import esWords from "an-array-of-spanish-words";
import CustomError from "../utils/CustomError";
import superagent from "superagent";
import { getConnection, getRepository } from "typeorm";
import { Definition } from "../entity/Definition";
import { Language } from "../entity/Language";
import { Pronunciation } from "../entity/Pronunciation";
import { SavedWord } from "../entity/SavedWord";
import { Word } from "../entity/Word";
import { getRandomNumber } from "../utils/getRandomNumber";
import { WordDetailJSON, WordDetailSimplifyJSON } from "../utils/types";
import {
  enClothing,
  enCommon,
  enEmotion,
  enFood,
  enMoney,
  enMovie,
  enMusic,
  enNature,
  enScience,
  enSport,
  enTraffic,
  enWeather,
} from "../utils/enWordTopics";
import {
  esClothing,
  esCommon,
  esEmotion,
  esFood,
  esMoney,
  esMovie,
  esMusic,
  esNature,
  esScience,
  esSport,
  esTraffic,
  esWeather,
} from "../utils/esWordTopics";
import {
  frClothing,
  frCommon,
  frEmotion,
  frFood,
  frMoney,
  frMovie,
  frMusic,
  frNature,
  frScience,
  frSport,
  frTraffic,
  frWeather,
} from "../utils/frWordTopics";
import {
  deClothing,
  deCommon,
  deEmotion,
  deFood,
  deMoney,
  deMovie,
  deMusic,
  deNature,
  deScience,
  deSport,
  deTraffic,
  deWeather,
} from "../utils/deWordTopics";

export const getDailyRandomWords = async (
  count: number,
  topic: string,
  language: string
) => {
  const results: WordDetailSimplifyJSON[] = [];
  let wordCount = count;
  let allWords: Array<string> = [];
  if (topic === "random") {
    switch (language) {
      case "en_US": {
        allWords = enWords;
        break;
      }
      case "de": {
        allWords = deWords;
        break;
      }
      case "fr": {
        allWords = frWords as Array<string>;
        break;
      }
      case "es": {
        allWords = esWords as Array<string>;
        break;
      }
      default: {
        allWords = [];
      }
    }
  } else {
    allWords = (
      await getRepository(Word)
        .createQueryBuilder("word")
        .where("LOWER(word.topics) LIKE LOWER(:topic)", { topic: `%${topic}%` })
        .andWhere("word.language = :language", { language })
        .getMany()
    ).map((word) => word.value);
  }

  console.log("all words results: ", allWords);

  const arr: any[] = [];
  while (results.length < wordCount) {
    let i = 0;
    const promises: Array<Promise<WordDetailSimplifyJSON | null>> = [];
    while (i < wordCount) {
      const num = getRandomNumber(0, allWords.length);
      const word = allWords[num];
      if (!arr.includes(word)) {
        arr.push(word);
        promises.push(getWordDetailSimplify(word, language));
        i++;
      }
    }

    await Promise.all(promises)
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
        throw new CustomError("Something went wrong!");
      });
  }

  // console.log("daily word results: ", results);

  return results;

  // switch (language) {
  //   case "en_US": {
  //     let i = 0;
  //     const arr: any[] = [];
  //     while (i < wordCount) {
  //       const num = getRandomNumber(0, 274936);
  //       const word = enWords[num];
  //       if (!arr.includes(word)) {
  //         const detail = await getWordDetailSimplify(word, language);
  //         if (detail) {
  //           results.push(detail);
  //           arr.push(word);
  //           i++;
  //         }
  //       }
  //     }
  //     console.log("tries: ", i);
  //     break;
  //   }
  //   case "de": {
  //     let i = 0;
  //     const arr: any[] = [];
  //     while (i < wordCount) {
  //       const num = getRandomNumber(0, 1680837);
  //       const word = deWords[num];
  //       if (!arr.includes(word)) {
  //         const detail = await getWordDetailSimplify(word, language);
  //         if (detail) {
  //           results.push(detail);
  //           arr.push(word);
  //           i++;
  //         }
  //       }
  //     }
  //     break;
  //   }
  //   case "fr": {
  //     let i = 0;
  //     const arr: any[] = [];
  //     while (i < wordCount) {
  //       const num = getRandomNumber(0, 336523);
  //       const word = (frWords as string[])[num];
  //       if (!arr.includes(word)) {
  //         const detail = await getWordDetailSimplify(word, language);
  //         if (detail) {
  //           results.push(detail);
  //           arr.push(word);
  //           i++;
  //         }
  //       }
  //     }
  //     break;
  //   }
  //   case "es": {
  //     let i = 0;
  //     const arr: any[] = [];
  //     while (i < wordCount) {
  //       const num = getRandomNumber(0, 636597);
  //       const word = (esWords as string[])[num];
  //       if (!arr.includes(word)) {
  //         const detail = await getWordDetailSimplify(word, language);
  //         if (detail) {
  //           results.push(detail);
  //           arr.push(word);
  //           i++;
  //         }
  //       }
  //     }
  //     break;
  //   }
  //   default: {
  //     throw new CustomError("Please provide supported language!");
  //   }
  // }
};

export const getWordDetail = async (wordString: string, language: string) => {
  const res = await superagent
    .get(
      `https://api.dictionaryapi.dev/api/v2/entries/${language}/${wordString}`
    )
    .catch(() => {
      return null;
    });

  if (res && res.body && res.body[0]) {
    const detail = res.body[0];
    const wordInDb = await getRepository(Word).findOne({
      value: wordString,
      language,
    });
    const topics = wordInDb ? wordInDb.topics : "unknown";
    const word: WordDetailJSON = {
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
    } else return null;

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
    } else return null;

    return word;
  } else return null;
};

export const getWordDetailSimplify = async (
  wordString: string,
  language: string
) => {
  const detail = await getWordDetail(wordString, language);
  const wordInDb = await getRepository(Word).findOne({
    value: wordString,
    language,
  });
  const topics = wordInDb ? wordInDb.topics : "unknown";
  if (detail && detail.definitions.length) {
    const word: WordDetailSimplifyJSON = {
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
  } else return null;
};

export const getWordAutoCompletes = async (language: string, text: string) => {
  const wordRepo = getRepository(Word);

  const autoCompletes = await wordRepo
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
    } as WordDetailSimplifyJSON;
  });
};

export const toggleSaveWord = async (
  word: string,
  language: string,
  userId: string
) => {
  const savedWordRepo = getRepository(SavedWord);
  const wordRepo = getRepository(Word);

  const savedWord = await savedWordRepo.findOne({
    value: word,
    language,
    userId,
  });

  if (savedWord) {
    // Remove saved word
    await savedWordRepo.delete({
      id: savedWord.id,
    });
  } else {
    /// Add to saved word list
    const detail = await getWordDetail(word, language);
    if (detail) {
      const queryRunner = getConnection().createQueryRunner();
      await queryRunner.connect();
      await queryRunner.startTransaction();

      try {
        const manager = queryRunner.manager;
        const length = await savedWordRepo.count({ userId, language });
        const wordInDb = await getRepository(Word).findOne({
          value: word,
          language,
        });
        const topics = wordInDb ? wordInDb.topics : "unknown";
        const savedWordDb = manager.create(SavedWord, {
          userId,
          value: word,
          language,
          topics,
          position: length + 1,
        });

        await manager.save(SavedWord, savedWordDb);

        for (const pron of detail.pronunciations) {
          await manager.insert(Pronunciation, {
            savedWordId: savedWordDb.id,
            symbol: pron.symbol,
            audio: pron.audio,
          });
        }

        if (detail.definitions.length == 0)
          throw new CustomError("No definition found!");
        for (let i = 0; i < detail.definitions.length; i++) {
          const definition = detail.definitions[i];
          await manager.insert(Definition, {
            savedWordId: savedWordDb.id,
            meaning: definition.meaning,
            example: definition.example,
            partOfSpeech: definition.partOfSpeech,
            position: i + 1,
          });
        }
        await queryRunner.commitTransaction();
        await queryRunner.release();
      } catch (error) {
        await queryRunner.rollbackTransaction();

        throw error;
      }
    } else throw new CustomError("Not found definition");
  }
};

export const getSavedWords = async (userId: string, language: string) => {
  const savedWordRepo = getRepository(SavedWord);
  const savedWords = await savedWordRepo
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
    } as WordDetailSimplifyJSON;
  });

  return simplifySavedWords;
};

export const rearrangeSavedWords = async (wordIds: string[]) => {
  const savedWordRepo = getRepository(SavedWord);

  await new Promise((resolve, reject) => {
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
};

export const rearrangeDefinition = async (definitionIds: string[]) => {
  const definitionRepo = getRepository(Definition);
  await new Promise((resolve, reject) => {
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
};

export const importAllLanguages = async () => {
  const languageRepo = getRepository(Language);

  if (!(await languageRepo.findOne({ id: "en_US" })))
    await languageRepo.insert({
      id: "en_US",
      value: "English",
    });
  if (!(await languageRepo.findOne({ id: "fr" })))
    await languageRepo.insert({
      id: "fr",
      value: "French",
    });
  if (!(await languageRepo.findOne({ id: "es" })))
    await languageRepo.insert({
      id: "es",
      value: "Spanish",
    });
  if (!(await languageRepo.findOne({ id: "de" })))
    await languageRepo.insert({
      id: "de",
      value: "German",
    });
};

export const importAllWords = async () => {
  const wordRepo = getRepository(Word);

  const enArr = await wordRepo.find({ language: "en_US" });
  if (!enArr.length) await importEnWords();
  console.log("Finish import all English words");

  const frArr = await wordRepo.find({ language: "fr" });
  if (!frArr.length) await importFrWords();
  console.log("Finish import all French words");

  const esArr = await wordRepo.find({ language: "es" });
  if (!esArr.length) await importEsWords();
  console.log("Finish import all Spanish words");

  const deArr = await wordRepo.find({ language: "de" });
  if (!deArr.length) await importDeWords();
  console.log("Finish import all German words");
};

const getEnWordTopicMap = () => {
  const map = {} as any;
  for (const word of enCommon) {
    map[word.toLowerCase()] = "common";
  }
  for (const word of enSport) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", sport";
    } else {
      map[word.toLowerCase()] = "sport";
    }
  }
  for (const word of enNature) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", nature";
    } else {
      map[word.toLowerCase()] = "nature";
    }
  }
  for (const word of enClothing) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", clothing";
    } else {
      map[word.toLowerCase()] = "clothing";
    }
  }
  for (const word of enEmotion) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", emotion";
    } else {
      map[word.toLowerCase()] = "emotion";
    }
  }
  for (const word of enFood) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", food";
    } else {
      map[word.toLowerCase()] = "food";
    }
  }
  for (const word of enMoney) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", money";
    } else {
      map[word.toLowerCase()] = "money";
    }
  }
  for (const word of enMovie) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", movie";
    } else {
      map[word.toLowerCase()] = "movie";
    }
  }
  for (const word of enMusic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] = ", music";
    } else {
      map[word.toLowerCase()] = "music";
    }
  }
  for (const word of enScience) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", science";
    } else {
      map[word.toLowerCase()] = "science";
    }
  }
  for (const word of enWeather) {
    if (map[word]) {
      map[word.toLowerCase()] += ", weather";
    } else {
      map[word.toLowerCase()] = "weather";
    }
  }
  for (const word of enTraffic) {
    if (map[word]) {
      map[word.toLowerCase()] += ", traffic";
    } else {
      map[word.toLowerCase()] = "traffic";
    }
  }
  return map;
};

const getEsWordTopicMap = () => {
  const map = {} as any;
  for (const word of esCommon) {
    map[word.toLowerCase()] = "common";
  }
  for (const word of esSport) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", sport";
    } else {
      map[word.toLowerCase()] = "sport";
    }
  }
  for (const word of esNature) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", nature";
    } else {
      map[word.toLowerCase()] = "nature";
    }
  }
  for (const word of esClothing) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", clothing";
    } else {
      map[word.toLowerCase()] = "clothing";
    }
  }
  for (const word of esEmotion) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", emotion";
    } else {
      map[word.toLowerCase()] = "emotion";
    }
  }
  for (const word of esFood) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", food";
    } else {
      map[word.toLowerCase()] = "food";
    }
  }
  for (const word of esMoney) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", money";
    } else {
      map[word.toLowerCase()] = "money";
    }
  }
  for (const word of esMovie) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", movie";
    } else {
      map[word.toLowerCase()] = "movie";
    }
  }
  for (const word of esMusic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] = ", music";
    } else {
      map[word.toLowerCase()] = "music";
    }
  }
  for (const word of esScience) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", science";
    } else {
      map[word.toLowerCase()] = "science";
    }
  }
  for (const word of esWeather) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", weather";
    } else {
      map[word.toLowerCase()] = "weather";
    }
  }
  for (const word of esTraffic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", traffic";
    } else {
      map[word.toLowerCase()] = "traffic";
    }
  }
  return map;
};

const getFrWordTopicMap = () => {
  const map = {} as any;
  for (const word of frCommon) {
    map[word.toLowerCase()] = "common";
  }
  for (const word of frSport) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", sport";
    } else {
      map[word.toLowerCase()] = "sport";
    }
  }
  for (const word of frNature) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", nature";
    } else {
      map[word.toLowerCase()] = "nature";
    }
  }
  for (const word of frClothing) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", clothing";
    } else {
      map[word.toLowerCase()] = "clothing";
    }
  }
  for (const word of frEmotion) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", emotion";
    } else {
      map[word.toLowerCase()] = "emotion";
    }
  }
  for (const word of frFood) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", food";
    } else {
      map[word.toLowerCase()] = "food";
    }
  }
  for (const word of frMoney) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", money";
    } else {
      map[word.toLowerCase()] = "money";
    }
  }
  for (const word of frMovie) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", movie";
    } else {
      map[word.toLowerCase()] = "movie";
    }
  }
  for (const word of frMusic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] = ", music";
    } else {
      map[word.toLowerCase()] = "music";
    }
  }
  for (const word of frScience) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", science";
    } else {
      map[word.toLowerCase()] = "science";
    }
  }
  for (const word of frWeather) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", weather";
    } else {
      map[word.toLowerCase()] = "weather";
    }
  }
  for (const word of frTraffic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", traffic";
    } else {
      map[word.toLowerCase()] = "traffic";
    }
  }
  return map;
};

const getDeWordTopicMap = () => {
  const map = {} as any;
  for (const word of deCommon) {
    map[word.toLowerCase()] = "common";
  }
  for (const word of deSport) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", sport";
    } else {
      map[word.toLowerCase()] = "sport";
    }
  }
  for (const word of deNature) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", nature";
    } else {
      map[word.toLowerCase()] = "nature";
    }
  }
  for (const word of deClothing) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", clothing";
    } else {
      map[word.toLowerCase()] = "clothing";
    }
  }
  for (const word of deEmotion) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", emotion";
    } else {
      map[word.toLowerCase()] = "emotion";
    }
  }
  for (const word of deFood) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", food";
    } else {
      map[word.toLowerCase()] = "food";
    }
  }
  for (const word of deMoney) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", money";
    } else {
      map[word.toLowerCase()] = "money";
    }
  }
  for (const word of deMovie) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", movie";
    } else {
      map[word.toLowerCase()] = "movie";
    }
  }
  for (const word of deMusic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] = ", music";
    } else {
      map[word.toLowerCase()] = "music";
    }
  }
  for (const word of deScience) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", science";
    } else {
      map[word.toLowerCase()] = "science";
    }
  }
  for (const word of deWeather) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", weather";
    } else {
      map[word.toLowerCase()] = "weather";
    }
  }
  for (const word of deTraffic) {
    if (map[word.toLowerCase()]) {
      map[word.toLowerCase()] += ", traffic";
    } else {
      map[word.toLowerCase()] = "traffic";
    }
  }
  return map;
};

const importEnWords = async () => {
  const wordRepo = getRepository(Word);
  const map = getEnWordTopicMap();
  for (const word of enWords) {
    const topics =
      map[word] && typeof map[word] == "string"
        ? (map[word] as string)
        : "unknown";
    await wordRepo.insert({
      value: word,
      language: "en_US",
      topics,
    });
  }
};

const importDeWords = async () => {
  const wordRepo = getRepository(Word);
  const map = getDeWordTopicMap();
  for (const word of deWords) {
    const topics =
      map[word] && typeof map[word] == "string"
        ? (map[word] as string)
        : "unknown";
    await wordRepo.insert({
      value: word,
      language: "de",
      topics,
    });
  }
};

const importFrWords = async () => {
  const wordRepo = getRepository(Word);
  const map = getFrWordTopicMap();
  for (const word of frWords as string[]) {
    const topics =
      map[word] && typeof map[word] == "string"
        ? (map[word] as string)
        : "unknown";
    await wordRepo.insert({
      value: word,
      language: "fr",
      topics,
    });
  }
};

const importEsWords = async () => {
  const wordRepo = getRepository(Word);
  const map = getEsWordTopicMap();
  for (const word of esWords as string[]) {
    const topics =
      map[word] && typeof map[word] == "string"
        ? (map[word] as string)
        : "unknown";
    await wordRepo.insert({
      value: word,
      language: "es",
      topics,
    });
  }
};
