"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const PurchaseController_1 = require("../controllers/PurchaseController");
const ApiResponse_1 = __importDefault(require("../utils/ApiResponse"));
const CustomError_1 = __importDefault(require("../utils/CustomError"));
const middlewares_1 = require("../utils/middlewares");
const router = express_1.Router();
router.post("/", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        console.log("purchasing premium...");
        const userId = req.user.id;
        const token = req.body.purchaseToken;
        const orderId = req.body.orderId;
        const purchaseTime = req.body.purchaseTime;
        console.log("purchase params", userId, token, orderId, purchaseTime);
        if (!userId) {
            throw new CustomError_1.default("Please log in first!");
        }
        if (!token) {
            throw new CustomError_1.default("Please provide purchase token!");
        }
        if (!orderId) {
            throw new CustomError_1.default("Please provide purchase order id!");
        }
        if (!purchaseTime) {
            throw new CustomError_1.default("Please provide purchase time!");
        }
        const result = yield PurchaseController_1.executePurchase(token, purchaseTime, orderId, userId);
        console.log("Purchase premium result", result);
        return res.send(new ApiResponse_1.default(true, "", result));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            return res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            return res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
exports.default = router;
//# sourceMappingURL=purchase.js.map