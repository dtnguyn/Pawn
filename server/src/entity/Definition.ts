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
export class Definition {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  savedWordId: string;
  @ManyToOne(() => SavedWord, (word) => word.definitions, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  @JoinColumn({ name: "savedWordId" })
  word: SavedWord;

  @Column()
  meaning: string;

  @Column()
  position: number;

  @Column({ nullable: true })
  partOfSpeech: string;

  @Column({ nullable: true })
  example: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
