import enWords from "an-array-of-english-words";
import frWords from "an-array-of-french-words";
import esWords from "an-array-of-spanish-words";
import deWords from "all-the-german-words";
import { getRandomNumber } from "../utils/getRandomNumber";
import superagent from "superagent";
import { WordJSON } from "../utils/types";
import { getConnection, getRepository } from "typeorm";
import { Word } from "../entity/Word";
import { SavedWord } from "../entity/SavedWord";
import { Pronunciation } from "../entity/Pronunciation";
import { Definition } from "../entity/Definition";
import { rejects } from "assert";

export const getDailyRandomWords = async (
  wordCount: number,
  language: string
) => {
  const results: WordJSON[] = [];

  switch (language) {
    case "en_US": {
      let i = 0;
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 274936);
        const word = enWords[num];

        if (!arr.includes(word)) {
          const def = await getDefinition(word, language);
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
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 1680837);
        const word = deWords[num];

        if (!arr.includes(word)) {
          const def = await getDefinition(word, language);
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
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 336523);
        const word = (frWords as string[])[num];

        if (!arr.includes(word)) {
          const def = await getDefinition(word, language);
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
      const arr: any[] = [];
      while (i < wordCount) {
        const num = getRandomNumber(0, 636597);
        const word = (esWords as string[])[num];

        if (!arr.includes(word)) {
          const def = await getDefinition(word, language);
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
};

export const getDefinition = async (word: string, language: string) => {
  const res = await superagent
    .get(`https://api.dictionaryapi.dev/api/v2/entries/${language}/${word}`)
    .catch(() => {
      return null;
    });

  if (res && res.body && res.body[0]) {
    const def = res.body[0];

    const word: WordJSON = {
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
    } else return null;

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
    } else return null;

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
  // const autoCompletes = await wordRepo.find({ language, value: text });
  return autoCompletes;
};

export const toggleSaveWord = async (
  word: string,
  language: string,
  userId: string
) => {
  const savedWordRepo = getRepository(SavedWord);
  const wordRepo = getRepository(Word);

  const savedWord = await savedWordRepo.findOne({
    wordValue: word,
    language,
    userId,
  });

  if (savedWord) {
    //Remove save word
    await savedWordRepo.delete({
      id: savedWord.id,
    });
  } else {
    const def = await getDefinition(word, language);

    const wordDb = await wordRepo.findOne({
      value: word,
      language,
    });

    if (!wordDb) throw new Error("Not found word!");

    if (def) {
      const queryRunner = getConnection().createQueryRunner();
      await queryRunner.connect();
      await queryRunner.startTransaction();
      try {
        const manager = queryRunner.manager;
        const length = await savedWordRepo.count({ userId, language });
        const savedWordDb = manager.create(SavedWord, {
          wordId: wordDb.id,
          userId,
          wordValue: word,
          language,
          position: length + 1,
        });

        await manager.save(SavedWord, savedWordDb);

        for (const pron of def.pronunciations) {
          await manager.insert(Pronunciation, {
            savedWordId: savedWordDb.id,
            symbol: pron.symbol,
            audio: pron.audio,
          });
        }

        for (let i = 0; i < def.definitions.length; i++) {
          const definition = def.definitions[i];
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
    } else throw new Error("Not found definition");
  }
};

export const getSavedWords = async (userId: string) => {
  const savedWordRepo = getRepository(SavedWord);

  const savedWords = await savedWordRepo
    .createQueryBuilder("savedWord")
    .leftJoinAndSelect("savedWord.word", "word")
    .leftJoinAndSelect("savedWord.pronunciations", "pronunciations")
    .leftJoinAndSelect("savedWord.definitions", "definitions")
    .orderBy("definitions.position", "ASC")
    .leftJoinAndSelect("savedWord.user", "user")
    .where("user.id = :userId", { userId })
    .orderBy("savedWord.position", "ASC")
    .getMany();

  // const savedWords = await savedWordRepo.find();
  console.log(savedWords);
  return savedWords;
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
