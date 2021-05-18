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
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  value: string;

  @Column*()
  learnerId: string
  @ManyToOne(() => User, (user) => user.learnedWords)
  learner: User;


  @Column()
  language: string;

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
