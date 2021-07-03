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
const LanguageController_1 = require("../controllers/LanguageController");
const auth_1 = require("./auth");
const router = express_1.Router();
router.get("/", auth_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const userId = req.user.id;
        const languages = yield LanguageController_1.getLearningLanguages(userId);
        res.json(new ApiResponse(true, "", languages));
    }
    catch (error) {
        res.status(400).json(new ApiResponse(false, error.message, null));
    }
}));
router.post("/save", auth_1.checkAuthentication, (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const languages = req.body.languages;
        const userId = req.user.id;
        yield LanguageController_1.chooseLanguages(languages, userId);
        res.json(new ApiResponse(true, "", null));
    }
    catch (error) {
        res.status(400).json(new ApiResponse(false, error.message, null));
    }
}));
exports.default = router;
//# sourceMappingURL=language.js.map