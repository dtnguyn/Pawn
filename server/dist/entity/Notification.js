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
const User_1 = require("./User");
let Notification = class Notification {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], Notification.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Notification.prototype, "senderId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => User_1.User, (user) => user.sentNotifications),
    __metadata("design:type", User_1.User)
], Notification.prototype, "sender", void 0);
__decorate([
    typeorm_1.ManyToMany(() => User_1.User, (user) => user.receiveNotifications),
    __metadata("design:type", User_1.User)
], Notification.prototype, "receivers", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Notification.prototype, "title", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Notification.prototype, "content", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], Notification.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], Notification.prototype, "updatedAt", void 0);
Notification = __decorate([
    typeorm_1.Entity()
], Notification);
exports.Notification = Notification;
//# sourceMappingURL=Notification.js.map