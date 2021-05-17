import {
  CreateDateColumn,
  Entity,
  ManyToMany,
  ManyToOne,
  PrimaryColumn,
  UpdateDateColumn,
} from "typeorm";
import { User } from "./User";

@Entity()
export class Language {
  @PrimaryColumn()
  id: string;

  @PrimaryColumn()
  value: string;

  @ManyToOne(() => User, (user) => user.nativeLanguage)
  natives: User[];

  @ManyToMany(() => User, (user) => user.learningLanguages)
  learners: User[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
