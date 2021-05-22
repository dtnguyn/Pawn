import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { SavedWord } from "./SavedWord";

@Entity()
export class Pronunciation {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  symbol: string;

  @PrimaryColumn()
  audio: string;

  @PrimaryColumn("uuid")
  savedWordId: string;
  @ManyToOne(() => SavedWord, (word) => word.pronunciations, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  @JoinColumn({ name: "savedWordId" })
  word: SavedWord;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
