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

export type FeedJSON = {
  id: string;
  type: string;
  title: string;
  thumbnail: string | null;
  author: string | null;
  topic: string | null;
  language: string;
  url: string;
  description: string;
  publishedDate: string | null;
};

export type FeedDetailJSON = {
  id: string;
  type: string;
  content: NewsDetailJSON | VideoDetailJSON;
};

export type NewsDetailJSON = {
  value: string;
};

export type VideoDetailJSON = {
  value: any;
};
