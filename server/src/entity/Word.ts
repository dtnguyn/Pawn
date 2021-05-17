import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToMany,
  ManyToOne,
  OneToMany,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { Definition } from "./Definition";
import { Notification } from "./Notification";
import { Pronunciation } from "./Pronunciation";
import { User } from "./User";

@Entity()
export class Word {
  @PrimaryColumn()
  value: string;

  @ManyToMany(() => User, (user) => user.learnedWords)
  learners: User[];

  @ManyToMany(() => User, (user) => user.ignoredWords)
  ignoredUsers: User[];

  @Column()
  type: string;

  @Column()
  language: string;

  @Column()
  position: string;

  @OneToMany(() => Definition, (definition) => definition.word)
  definitions: Definition[];

  @OneToMany(() => Pronunciation, (pronunciation) => pronunciation.word)
  pronunciations: Pronunciation[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
