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
const ApiResponse_1 = __importDefault(require("../utils/ApiResponse"));
const WordController_1 = require("../controllers/WordController");
const middlewares_1 = require("../utils/middlewares");
const router = express_1.Router();
router.get("/daily", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        let dailyWordCount = 3;
        if (req.body.dailyWordCount) {
            dailyWordCount = req.body.dailyWordCount;
        }
        const language = req.body.language;
        if (language)
            return res.send(new ApiResponse_1.default(false, "Please provide the target language!", null));
        const words = yield WordController_1.getDailyRandomWords(dailyWordCount, language);
        return res.send(new ApiResponse_1.default(true, "", words));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.get("/definition", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const language = req.query.language;
        const word = req.query.word;
        if (!word || !language)
            throw new Error("Please provide the word and definition language");
        const definition = yield WordController_1.getDefinition(word, language);
        if (!definition)
            throw new Error("Couldn't find definition for the word provided!");
        return res.send(new ApiResponse_1.default(true, "", definition));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.get("/autocomplete", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const text = req.query.text;
        const language = req.query.language;
        if (!text || !language)
            throw new Error("Please provide text and language!");
        const results = yield WordController_1.getWordAutoCompletes(language, text);
        return res.send(new ApiResponse_1.default(true, "", results));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.get("/save", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const userId = req.user.id;
        const savedWords = yield WordController_1.getSavedWords(userId);
        return res.send(new ApiResponse_1.default(true, "", savedWords));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.post("/save", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const word = req.body.word;
        const language = req.body.language;
        const userId = req.user.id;
        yield WordController_1.toggleSaveWord(word, language, userId);
        return res.send(new ApiResponse_1.default(true, "", null));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.patch("/rearrange", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const wordIds = req.body.wordIds;
        if (!wordIds || !wordIds.length)
            throw new Error("Please provide a list of saved words!");
        yield WordController_1.rearrangeSavedWords(wordIds);
        return res.send(new ApiResponse_1.default(true, "", null));
    }
    catch (error) {
        console.log(error);
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
router.patch("/definition/rearrange", middlewares_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const definitionIds = req.body.definitionIds;
        if (!definitionIds || !definitionIds.length)
            throw new Error("Please provide a list of definitions!");
        yield WordController_1.rearrangeDefinition(definitionIds);
        return res.send(new ApiResponse_1.default(true, "", null));
    }
    catch (error) {
        return res.send(new ApiResponse_1.default(false, error.message, null));
    }
}));
exports.default = router;
//# sourceMappingURL=word.js.map