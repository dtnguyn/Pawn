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
const CustomError_1 = __importDefault(require("../utils/CustomError"));
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
        var feedTopics = req.user.feedTopics;
        if (feedTopics == undefined || feedTopics == null) {
            feedTopics = "";
        }
        const language = req.query.language;
        if (!language) {
            throw new CustomError_1.default("Please provide target language!");
        }
        const words = yield WordController_1.getSavedWords(userId, language);
        const feeds = yield FeedController_1.getFeeds(words, language, feedTopics);
        console.log("feeds length ", feeds.length);
        res.send(new ApiResponse_1.default(true, "", feeds));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            console.log("get feed error ", error.message);
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
router.get("/topics", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const user = req.user;
        if (!user.id) {
            throw new CustomError_1.default("Please login first!");
        }
        res.send(new ApiResponse_1.default(true, "", user.feedTopics));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            console.log("get feed error ", error.message);
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
router.post("/topics", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const userId = req.user.id;
        if (!userId) {
            throw new CustomError_1.default("Please login first!");
        }
        const newTopicsString = req.body.newTopics;
        if (!newTopicsString) {
            throw new CustomError_1.default("Invalid input for updating topics!");
        }
        yield FeedController_1.updateTopics(userId, newTopicsString);
        res.send(new ApiResponse_1.default(true, "", newTopicsString));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            console.log("get feed error ", error.message);
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
router.get("/detail", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const feedType = req.query.type;
        if (!feedType) {
            throw new CustomError_1.default("Please provide feed type!");
        }
        const feedUrl = req.query.url;
        if (!feedUrl) {
            throw new CustomError_1.default("Please provide feed url!");
        }
        const feedId = req.query.id;
        if (!feedUrl) {
            throw new CustomError_1.default("Please provide feed id!");
        }
        const feedDetail = yield FeedController_1.getFeedDetail(feedId, feedType, feedUrl);
        res.send(new ApiResponse_1.default(true, "", feedDetail));
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            console.log("get feed error ", error.message);
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
        }
    }
}));
exports.default = router;
//# sourceMappingURL=feed.js.map