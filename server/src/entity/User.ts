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
import { GroupChat } from "./GroupChat";
import { Language } from "./Language";
import { Notification } from "./Notification";
import { Topic } from "./Topic";
import { UserRefreshToken } from "./UserRefreshToken";
import { Word } from "./Word";

@Entity()
export class User {
  @PrimaryGeneratedColumn("uuid")
  id: string;

  @Column({ nullable: true })
  oauthId: string;

  @Column()
  username: string;

  @Column({ nullable: true })
  password: string;

  @Column({ nullable: true })
  email: string;

  @Column({ nullable: true })
  avatar: string;

  @Column({ default: 3 })
  dailyWordCount: number;

  @Column({ default: true })
  notificationEnabled: boolean;

  @OneToMany(() => UserRefreshToken, (token) => token.user)
  refreshTokens: UserRefreshToken[];

  @ManyToMany(() => Notification, (notification) => notification.receivers)
  @JoinTable()
  receiveNotifications: Notification;

  @OneToMany(() => Notification, (notification) => notification.sender)
  sentNotifications: Notification;

  @Column()
  nativeLanguageId: string;
  @ManyToOne(() => Language, (language) => language.natives)
  nativeLanguage: Language;

  @ManyToMany(() => Topic, (topic) => topic.users)
  @JoinTable()
  feedTopics: Topic[];

  @ManyToMany(() => Language, (language) => language.natives)
  @JoinTable()
  learningLanguages: Language[];

  @ManyToMany(() => GroupChat, (groupChat) => groupChat.members)
  @JoinTable()
  followedGroupChats: GroupChat[];

  @OneToMany(() => GroupChat, (groupChat) => groupChat.admin)
  ownedGroupChat: ChatImage[];

  @OneToMany(() => ChatImage, (image) => image.user)
  chatImages: ChatImage[];

  @OneToMany(() => ChatImage, (image) => image.user)
  chatMessages: ChatMessage[];

  @ManyToMany(() => Word, (word) => word.learners)
  @JoinTable()
  learnedWords: Word[];

  @ManyToMany(() => Word, (word) => word.ignoredUsers)
  @JoinTable()
  ignoredWords: Word[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
