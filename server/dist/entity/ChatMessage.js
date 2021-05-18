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
const GroupChat_1 = require("./GroupChat");
const User_1 = require("./User");
let ChatMessage = class ChatMessage {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], ChatMessage.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatMessage.prototype, "userId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => User_1.User, (user) => user.chatMessages),
    __metadata("design:type", User_1.User)
], ChatMessage.prototype, "user", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatMessage.prototype, "groupChatId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => GroupChat_1.GroupChat, (groupChat) => groupChat.messages),
    __metadata("design:type", GroupChat_1.GroupChat)
], ChatMessage.prototype, "groupChat", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], ChatMessage.prototype, "message", void 0);
__decorate([
    typeorm_1.OneToMany(() => ChatImage_1.ChatImage, (image) => image.chatMessage),
    __metadata("design:type", Array)
], ChatMessage.prototype, "images", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], ChatMessage.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], ChatMessage.prototype, "updatedAt", void 0);
ChatMessage = __decorate([
    typeorm_1.Entity()
], ChatMessage);
exports.ChatMessage = ChatMessage;
//# sourceMappingURL=ChatMessage.js.map