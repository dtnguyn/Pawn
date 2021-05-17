import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from "typeorm";
import { ChatMessage } from "./ChatMessage";
import { GroupChat } from "./GroupChat";
import { User } from "./User";

@Entity()
export class ChatImage {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column()
  userId: string;
  @ManyToOne(() => User, (user) => user.chatImages)
  user: User;

  @Column()
  chatMessageId: string;
  @ManyToOne(() => ChatMessage, (message) => message.images)
  chatMessage: ChatMessage;

  @Column()
  groupChatId: string;
  @ManyToOne(() => GroupChat, (groupChat) => groupChat.images)
  groupChat: GroupChat;

  @Column()
  url: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
