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
const ChatImage_1 = require("./ChatImage");
const ChatMessage_1 = require("./ChatMessage");
const User_1 = require("./User");
let GroupChat = class GroupChat {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], GroupChat.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], GroupChat.prototype, "adminId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => User_1.User, (admin) => admin.ownedGroupChat),
    __metadata("design:type", User_1.User)
], GroupChat.prototype, "admin", void 0);
__decorate([
    typeorm_1.ManyToMany(() => User_1.User, (users) => users.followedGroupChats),
    __metadata("design:type", Array)
], GroupChat.prototype, "members", void 0);
__decorate([
    typeorm_1.OneToMany(() => ChatImage_1.ChatImage, (image) => image.groupChat),
    __metadata("design:type", Array)
], GroupChat.prototype, "images", void 0);
__decorate([
    typeorm_1.OneToMany(() => ChatMessage_1.ChatMessage, (message) => message.groupChat),
    __metadata("design:type", Array)
], GroupChat.prototype, "messages", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], GroupChat.prototype, "name", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], GroupChat.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], GroupChat.prototype, "updatedAt", void 0);
GroupChat = __decorate([
    typeorm_1.Entity()
], GroupChat);
exports.GroupChat = GroupChat;
//# sourceMappingURL=GroupChat.js.map