import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToMany,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { ChatImage } from "./ChatImage";
import { ChatMessage } from "./ChatMessage";
import { User } from "./User";

@Entity()
export class GroupChat {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  adminId: string;
  @ManyToOne(() => User, (admin) => admin.ownedGroupChat)
  admin: User;

  @ManyToMany(() => User, (users) => users.followedGroupChats)
  members: User[];

  @OneToMany(() => ChatImage, (image) => image.groupChat)
  images: ChatImage[];

  @OneToMany(() => ChatMessage, (message) => message.groupChat)
  messages: ChatMessage[];

  @Column()
  name: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
