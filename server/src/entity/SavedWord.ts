import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  OneToMany,
  OneToOne,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { Definition } from "./Definition";
import { Pronunciation } from "./Pronunciation";
import { User } from "./User";
import { Word } from "./Word";

@Entity()
export class SavedWord {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  wordValue: string;
  @ManyToOne(() => Word, (word) => word.savedWords)
  word: Word;

  @Column()
  userId: string;
  @ManyToOne(() => User, (user) => user.savedWords)
  user: User;

  @Column({ default: "0" })
  position: number;

  @OneToMany(() => Definition, (definition) => definition.word)
  definitions: Definition[];

  @OneToMany(() => Pronunciation, (pronunciation) => pronunciation.word)
  pronunciations: Pronunciation[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
