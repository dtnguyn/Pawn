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

export const getDailyRandomWords = async (
  wordCount: number,
  language: string
) => {
  const results: WordDetailSimplifyJSON[] = [];

  switch (language) {
    case "en_US": {
      let i = 0;
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 274936);
        const word = enWords[num];
        if (!arr.includes(word)) {
          const detail = await getWordDetailSimplify(word, language);
          if (detail) {
            results.push(detail);
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
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 1680837);
        const word = deWords[num];
        if (!arr.includes(word)) {
          const detail = await getWordDetailSimplify(word, language);
          if (detail) {
            results.push(detail);
            arr.push(word);
            i++;
          }
        }
      }
      break;
    }
    case "fr": {
      let i = 0;
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 336523);
        const word = (frWords as string[])[num];
        if (!arr.includes(word)) {
          const detail = await getWordDetailSimplify(word, language);
          if (detail) {
            results.push(detail);
            arr.push(word);
            i++;
          }
        }
      }
      break;
    }
    case "es": {
      let i = 0;
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 636597);
        const word = (esWords as string[])[num];
        if (!arr.includes(word)) {
          const detail = await getWordDetailSimplify(word, language);
          if (detail) {
            results.push(detail);
            arr.push(word);
            i++;
          }
        }
      }
      break;
    }
    default: {
      throw new CustomError("Please provide supported language!");
    }
  }

  return results;
};

export const getWordDetail = async (word: string, language: string) => {
  const res = await superagent
    .get(`https://api.dictionaryapi.dev/api/v2/entries/${language}/${word}`)
    .catch(() => {
      return null;
    });

  if (res && res.body && res.body[0]) {
    const detail = res.body[0];

    const word: WordDetailJSON = {
      value: detail.word,
      language,
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

export const getWordDetailSimplify = async (word: string, language: string) => {
  const detail = await getWordDetail(word, language);
  if (detail && detail.definitions.length) {
    const word: WordDetailSimplifyJSON = {
      value: detail.value,
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
        const savedWordDb = manager.create(SavedWord, {
          userId,
          value: word,
          language,
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

const importEnWords = async () => {
  const wordRepo = getRepository(Word);
  for (const word of enWords) {
    await wordRepo.insert({
      value: word,
      language: "en_US",
    });
  }
};

const importDeWords = async () => {
  const wordRepo = getRepository(Word);
  for (const word of deWords) {
    await wordRepo.insert({
      value: word,
      language: "de",
    });
  }
};

const importFrWords = async () => {
  const wordRepo = getRepository(Word);
  for (const word of frWords as string[]) {
    await wordRepo.insert({
      value: word,
      language: "fr",
    });
  }
};

const importEsWords = async () => {
  const wordRepo = getRepository(Word);
  for (const word of esWords as string[]) {
    await wordRepo.insert({
      value: word,
      language: "es",
    });
  }
};
