import enWords from "an-array-of-english-words";
import frWords from "an-array-of-french-words";
import esWords from "an-array-of-spanish-words";
import deWords from "all-the-german-words";
import { getRandomNumber } from "../utils/getRandomNumber";
import superagent from "superagent";
import { WordJSON } from "src/utils/types";

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
        const word = frWords[num];

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
        const word = esWords[num];

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

const getDefinition = async (word: string, language: string) => {
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
