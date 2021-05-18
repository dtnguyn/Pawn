export type WordJSON = {
  value: string;
  language: string;
  pronunciations: PronunciationJSON[];
  definitions: DefinitionJSON[];
};

export type PronunciationJSON = {
  audio: string;
  symbol: string;
};

export type DefinitionJSON = {
  meaning: string;
  partOfSpeech: string;
  example: string;
};
