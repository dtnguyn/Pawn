export type WordDetailJSON = {
  value: string;
  language: string;
  pronunciations: PronunciationJSON[];
  definitions: DefinitionJSON[];
};

export type WordDetailSimplifyJSON = {
  value: string;
  language: string;
  mainDefinition: string;
  pronunciationAudio: string | null;
  pronunciationSymbol: string | null;
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
