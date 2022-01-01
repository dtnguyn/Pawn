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
let SavedWord = class SavedWord {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], SavedWord.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], SavedWord.prototype, "value", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], SavedWord.prototype, "language", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], SavedWord.prototype, "topics", void 0);
__decorate([
    typeorm_1.Column("uuid"),
    __metadata("design:type", String)
], SavedWord.prototype, "userId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => User_1.User, (user) => user.savedWords, {
        onDelete: "CASCADE",
        onUpdate: "CASCADE",
    }),
    typeorm_1.JoinColumn({ name: "userId" }),
    __metadata("design:type", User_1.User)
], SavedWord.prototype, "user", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", Number)
], SavedWord.prototype, "position", void 0);
__decorate([
    typeorm_1.OneToMany(() => Definition_1.Definition, (definition) => definition.word),
    __metadata("design:type", Array)
], SavedWord.prototype, "definitions", void 0);
__decorate([
    typeorm_1.OneToMany(() => Pronunciation_1.Pronunciation, (pronunciation) => pronunciation.word),
    __metadata("design:type", Array)
], SavedWord.prototype, "pronunciations", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], SavedWord.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], SavedWord.prototype, "updatedAt", void 0);
SavedWord = __decorate([
    typeorm_1.Entity()
], SavedWord);
exports.SavedWord = SavedWord;
//# sourceMappingURL=SavedWord.js.map