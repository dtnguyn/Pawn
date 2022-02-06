"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const Purchase_1 = require("../entity/Purchase");
const typeorm_1 = require("typeorm");
const User_1 = require("../entity/User");
const isPurchaseValid = (token) => __awaiter(this, void 0, void 0, function* () {
    const repo = typeorm_1.getRepository(Purchase_1.Purchase);
    const results = yield repo.find({ purchaseToken: token });
    return results.length == 0;
});
exports.executePurchase = (token, time, orderId, userId) => __awaiter(this, void 0, void 0, function* () {
    const result = yield isPurchaseValid(token);
    if (result) {
        const repo = typeorm_1.getRepository(Purchase_1.Purchase);
        yield repo.insert({
            orderId,
            purchaseTime: time,
            isValid: true,
            purchaseToken: token,
        });
        const userRepo = typeorm_1.getRepository(User_1.User);
        yield userRepo.update({ id: userId }, { isPremium: true });
        return true;
    }
    else {
        return false;
    }
});
//# sourceMappingURL=PurchaseController.js.map