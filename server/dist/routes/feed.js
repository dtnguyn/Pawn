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
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
const express_1 = require("express");
const CustomError_1 = __importDefault(require("src/utils/CustomError"));
const FeedController_1 = require("../controllers/FeedController");
const WordController_1 = require("../controllers/WordController");
const ApiResponse_1 = __importDefault(require("../utils/ApiResponse"));
const middlewares_1 = require("../utils/middlewares");
const router = express_1.Router();
router.get("/", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const userId = req.user.id;
        if (!userId) {
            throw new CustomError_1.default("Please login first!");
        }
        const language = req.query.language;
        const words = yield WordController_1.getSavedWords(userId, language);
        const feeds = yield FeedController_1.getFeeds(words, language);
        res.send(new ApiResponse_1.default(true, "", feeds));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
exports.default = router;
//# sourceMappingURL=feed.js.map