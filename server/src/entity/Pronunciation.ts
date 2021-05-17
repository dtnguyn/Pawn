import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
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
  @ManyToOne(() => Word, (word) => word.pronunciations)
  word: Word;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
