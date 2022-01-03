import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  OneToMany,
  ManyToMany,
  JoinTable,
  ManyToOne,
} from "typeorm";
import { ChatImage } from "./ChatImage";
import { ChatMessage } from "./ChatMessage";
import { DailyWord } from "./DailyWord";
import { GroupChat } from "./GroupChat";
import { Language } from "./Language";
import { Notification } from "./Notification";
import { SavedWord } from "./SavedWord";
import { Topic } from "./Topic";
import { UserRefreshToken } from "./UserRefreshToken";
import { Word } from "./Word";

@Entity()
export class User {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column({ nullable: true })
  oauthId: string;

  @Column({ unique: true })
  username: string;

  @Column({ nullable: true })
  password: string;

  @Column({ unique: true })
  email: string;

  @Column({ nullable: true, default: "human1" })
  avatar: string;

  @Column({ default: 3 })
  dailyWordCount: number;

  @Column({ default: "" })
  feedTopics: string;

  @Column({ default: "Random" })
  dailyWordTopic: string;

  @Column({ default: true })
  notificationEnabled: boolean;

  @OneToMany(() => SavedWord, (word) => word.user)
  savedWords: SavedWord[];

  @OneToMany(() => UserRefreshToken, (token) => token.user)
  refreshTokens: UserRefreshToken[];

  @ManyToMany(() => Notification, (notification) => notification.receivers)
  @JoinTable()
  receiveNotifications: Notification;

  @OneToMany(() => Notification, (notification) => notification.sender)
  sentNotifications: Notification;

  @Column()
  nativeLanguageId: string;

  @Column()
  appLanguageId: string;

  @ManyToMany(() => Language, (language) => language.learners, {
    cascade: ["insert"],
  })
  @JoinTable()
  learningLanguages: Language[];

  // @ManyToMany(() => GroupChat, (groupChat) => groupChat.members)
  // @JoinTable()
  // followedGroupChats: GroupChat[];

  // @OneToMany(() => GroupChat, (groupChat) => groupChat.admin)
  // ownedGroupChat: ChatImage[];

  // @OneToMany(() => ChatImage, (image) => image.user)
  // chatImages: ChatImage[];

  // @OneToMany(() => ChatImage, (image) => image.user)
  // chatMessages: ChatMessage[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
