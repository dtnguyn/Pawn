import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  PrimaryColumn,
  UpdateDateColumn,
} from "typeorm";
import { User } from "./User";

@Entity()
export class UserRefreshToken {
  @PrimaryColumn()
  token: string;

  @Column()
  userId: string;
  @ManyToOne(() => User, (user) => user.refreshTokens, {
    onDelete: "CASCADE",
    onUpdate: "CASCADE",
  })
  user: User;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
