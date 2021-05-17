import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { User } from "./User";

@Entity()
export class Topic {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @ManyToMany(() => User, (user) => user.feedTopics)
  users: User[];

  @Column()
  value: string;

  @Column()
  icon: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
