import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { Word } from "./Word";

@Entity()
export class Definition {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  wordId: string;
  @ManyToOne(() => Word, (word) => word.definitions)
  word: Word;

  @Column()
  meaning: string;

  @Column()
  example: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
