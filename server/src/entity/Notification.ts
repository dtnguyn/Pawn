import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToMany,
  ManyToOne,
  PrimaryColumn,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { User } from "./User";
import { Word } from "./Word";

@Entity()
export class Notification {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  senderId: string;
  @ManyToOne(() => User, (user) => user.sentNotifications)
  sender: User;

  @ManyToMany(() => User, (user) => user.receiveNotifications)
  receivers: User;

  @Column()
  title: string;

  @Column()
  content: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
