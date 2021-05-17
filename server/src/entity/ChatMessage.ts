import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { ChatImage } from "./ChatImage";
import { GroupChat } from "./GroupChat";
import { User } from "./User";

@Entity()
export class ChatMessage {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  userId: string;
  @ManyToOne(() => User, (user) => user.chatMessages)
  user: User;

  @Column()
  groupChatId: string;
  @ManyToOne(() => GroupChat, (groupChat) => groupChat.messages)
  groupChat: GroupChat;

  @Column()
  message: string;

  @OneToMany(() => ChatImage, (image) => image.chatMessage)
  images: ChatImage[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
