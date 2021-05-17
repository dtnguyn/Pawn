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
const Word_1 = require("./Word");
let Definition = class Definition {
};
__decorate([
    typeorm_1.PrimaryGeneratedColumn("uuid"),
    __metadata("design:type", String)
], Definition.prototype, "id", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Definition.prototype, "wordId", void 0);
__decorate([
    typeorm_1.ManyToOne(() => Word_1.Word, (word) => word.definitions),
    __metadata("design:type", Word_1.Word)
], Definition.prototype, "word", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Definition.prototype, "meaning", void 0);
__decorate([
    typeorm_1.Column(),
    __metadata("design:type", String)
], Definition.prototype, "example", void 0);
__decorate([
    typeorm_1.CreateDateColumn(),
    __metadata("design:type", Date)
], Definition.prototype, "createdAt", void 0);
__decorate([
    typeorm_1.UpdateDateColumn(),
    __metadata("design:type", Date)
], Definition.prototype, "updatedAt", void 0);
Definition = __decorate([
    typeorm_1.Entity()
], Definition);
exports.Definition = Definition;
//# sourceMappingURL=Definition.js.map