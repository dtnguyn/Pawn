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
let Topic = class Topic {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], Topic.prototype, "id", void 0);
__decorate([
    typeorm_1.ManyToMany(() => User_1.User, (user) => user.feedTopics),
    __metadata("design:type", Array)
], Topic.prototype, "users", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Topic.prototype, "value", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Topic.prototype, "icon", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], Topic.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], Topic.prototype, "updatedAt", void 0);
Topic = __decorate([
    typeorm_1.Entity()
], Topic);
exports.Topic = Topic;
//# sourceMappingURL=Topic.js.map