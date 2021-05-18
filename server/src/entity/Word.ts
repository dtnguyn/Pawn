import {
  Column,
  CreateDateColumn,
  Entity,
  OneToMany,
  PrimaryColumn,
  UpdateDateColumn,
} from "typeorm";
import { SavedWord } from "./SavedWord";

@Entity()
export class Word {
  @PrimaryColumn()
  value: string;

  @PrimaryColumn()
  language: string;

  @OneToMany(() => SavedWord, (savedWord) => savedWord.word)
  savedWords: SavedWord[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
