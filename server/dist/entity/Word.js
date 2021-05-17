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
const Definition_1 = require("./Definition");
const Pronunciation_1 = require("./Pronunciation");
const User_1 = require("./User");
let Word = class Word {
};
__decorate([
    typeorm_1.PrimaryColumn(),
    __metadata("design:type", String)
], Word.prototype, "value", void 0);
__decorate([
    typeorm_1.ManyToMany(() => User_1.User, (user) => user.learnedWords),
    __metadata("design:type", Array)
], Word.prototype, "learners", void 0);
__decorate([
    typeorm_1.ManyToMany(() => User_1.User, (user) => user.ignoredWords),
    __metadata("design:type", Array)
], Word.prototype, "ignoredUsers", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Word.prototype, "type", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Word.prototype, "language", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Word.prototype, "position", void 0);
__decorate([
    typeorm_1.OneToMany(() => Definition_1.Definition, (definition) => definition.word),
    __metadata("design:type", Array)
], Word.prototype, "definitions", void 0);
__decorate([
    typeorm_1.OneToMany(() => Pronunciation_1.Pronunciation, (pronunciation) => pronunciation.word),
    __metadata("design:type", Array)
], Word.prototype, "pronunciations", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], Word.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], Word.prototype, "updatedAt", void 0);
Word = __decorate([
    typeorm_1.Entity()
], Word);
exports.Word = Word;
//# sourceMappingURL=Word.js.map