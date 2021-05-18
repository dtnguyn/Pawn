import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { SavedWord } from "./SavedWord";
import { Word } from "./Word";

@Entity()
export class Pronunciation {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  symbol: string;

  @Column()
  audio: string;

  @Column()
  wordId: string;
  @ManyToOne(() => SavedWord, (word) => word.pronunciations)
  word: SavedWord;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
