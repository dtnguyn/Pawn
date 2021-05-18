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
const express_1 = require("express");
const WordController_1 = require("../controllers/WordController");
const router = express_1.Router();
router.get("/daily", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        let dailyWordCount = 3;
        if (req.user) {
            dailyWordCount = req.user.dailyWordCount;
        }
        const language = req.query.language;
        const words = yield WordController_1.getDailyRandomWords(dailyWordCount, language);
        res.json(words);
    }
    catch (error) {
        res.status(400).send({
            message: error.message,
        });
    }
}));
router.get("/definition", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const language = req.query.language;
        const word = req.query.word;
        if (!word || !language)
            throw new Error("Please provide the word and definition language");
        const definition = yield WordController_1.getDefinition(word, language);
        res.json(definition);
    }
    catch (error) {
        res.status(400).send({
            message: error.message,
        });
    }
}));
exports.default = router;
//# sourceMappingURL=word.js.map