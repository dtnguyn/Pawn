import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryColumn,
  ,
  UpdateDateColumn,
} from "typeorm";
import { User } from "./User";
import { Word } from "./Word";

@Entity()
export class UserRefreshToken {
  @PrimaryColumn()
  token: string;

  @Column()
  userId: string;
  @ManyToOne(() => User, (user) => user.refreshTokens)
  user: User;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
