import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { SavedWord } from "./SavedWord";
import { Word } from "./Word";

@Entity()
export class Definition {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  wordId: string;
  @ManyToOne(() => SavedWord, (word) => word.definitions)
  word: SavedWord;

  @Column()
  meaning: string;

  @Column()
  partOfSpeech: string;

  @Column()
  example: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
