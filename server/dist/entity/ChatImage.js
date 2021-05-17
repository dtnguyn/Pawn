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
const ChatMessage_1 = require("./ChatMessage");
const GroupChat_1 = require("./GroupChat");
const User_1 = require("./User");
let ChatImage = class ChatImage {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], ChatImage.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatImage.prototype, "userId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => User_1.User, (user) => user.chatImages),
    __metadata("design:type", User_1.User)
], ChatImage.prototype, "user", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatImage.prototype, "chatMessageId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => ChatMessage_1.ChatMessage, (message) => message.images),
    __metadata("design:type", ChatMessage_1.ChatMessage)
], ChatImage.prototype, "chatMessage", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatImage.prototype, "groupChatId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => GroupChat_1.GroupChat, (groupChat) => groupChat.images),
    __metadata("design:type", GroupChat_1.GroupChat)
], ChatImage.prototype, "groupChat", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatImage.prototype, "url", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], ChatImage.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], ChatImage.prototype, "updatedAt", void 0);
ChatImage = __decorate([
    typeorm_1.Entity()
], ChatImage);
exports.ChatImage = ChatImage;
//# sourceMappingURL=ChatImage.js.map