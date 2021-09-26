"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
const typeorm_1 = require("typeorm");
const Language_1 = require("./Language");
const Notification_1 = require("./Notification");
const SavedWord_1 = require("./SavedWord");
const Topic_1 = require("./Topic");
const UserRefreshToken_1 = require("./UserRefreshToken");
let User = class User {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], User.prototype, "id", void 0);
__decorate([
    typeorm_1.Column({ nullable: true }),
    __metadata("design:type", String)
], User.prototype, "oauthId", void 0);
__decorate([
    typeorm_1.Column({ unique: true }),
    __metadata("design:type", String)
], User.prototype, "username", void 0);
__decorate([
    typeorm_1.Column({ nullable: true }),
    __metadata("design:type", String)
], User.prototype, "password", void 0);
__decorate([
    typeorm_1.Column({ unique: true }),
    __metadata("design:type", String)
], User.prototype, "email", void 0);
__decorate([
    typeorm_1.Column({ nullable: true }),
    __metadata("design:type", String)
], User.prototype, "avatar", void 0);
__decorate([
    typeorm_1.Column({ default: 3 }),
    __metadata("design:type", Number)
], User.prototype, "dailyWordCount", void 0);
__decorate([
    typeorm_1.Column({ default: "" }),
    __metadata("design:type", String)
], User.prototype, "feedTopics", void 0);
__decorate([
    typeorm_1.Column({ default: true }),
    __metadata("design:type", Boolean)
], User.prototype, "notificationEnabled", void 0);
__decorate([
    typeorm_1.OneToMany(() => SavedWord_1.SavedWord, (word) => word.user),
    __metadata("design:type", Array)
], User.prototype, "savedWords", void 0);
__decorate([
    typeorm_1.OneToMany(() => UserRefreshToken_1.UserRefreshToken, (token) => token.user),
    __metadata("design:type", Array)
], User.prototype, "refreshTokens", void 0);
__decorate([
    typeorm_1.ManyToMany(() => Notification_1.Notification, (notification) => notification.receivers),
    typeorm_1.JoinTable(),
    __metadata("design:type", Notification_1.Notification)
], User.prototype, "receiveNotifications", void 0);
__decorate([
    typeorm_1.OneToMany(() => Notification_1.Notification, (notification) => notification.sender),
    __metadata("design:type", Notification_1.Notification)
], User.prototype, "sentNotifications", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], User.prototype, "nativeLanguageId", void 0);
__decorate([
    typeorm_1.ManyToMany(() => Language_1.Language, (language) => language.learners, {
        cascade: ["insert"],
    }),
    typeorm_1.JoinTable(),
    __metadata("design:type", Array)
], User.prototype, "learningLanguages", void 0);
__decorate([
    typeorm_1.ManyToMany(() => Topic_1.Topic, (topic) => topic.users),
    typeorm_1.JoinTable(),
    __metadata("design:type", Array)
], User.prototype, "feedTopics", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], User.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], User.prototype, "updatedAt", void 0);
User = __decorate([
    typeorm_1.Entity()
], User);
exports.User = User;
//# sourceMappingURL=User.js.map