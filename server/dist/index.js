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
require("reflect-metadata");
const typeorm_1 = require("typeorm");
const ChatImage_1 = require("./entity/ChatImage");
const ChatMessage_1 = require("./entity/ChatMessage");
const Definition_1 = require("./entity/Definition");
const GroupChat_1 = require("./entity/GroupChat");
const Language_1 = require("./entity/Language");
const Notification_1 = require("./entity/Notification");
const Pronunciation_1 = require("./entity/Pronunciation");
const Topic_1 = require("./entity/Topic");
const User_1 = require("./entity/User");
const Word_1 = require("./entity/Word");
const express_1 = __importDefault(require("express"));
const auth_1 = __importDefault(require("./routes/auth"));
const word_1 = __importDefault(require("./routes/word"));
const UserRefreshToken_1 = require("./entity/UserRefreshToken");
const SavedWord_1 = require("./entity/SavedWord");
typeorm_1.createConnection({
    type: "postgres",
    host: "localhost",
    port: 5432,
    username: "postgres",
    password: "postgres",
    database: "pawn_dev",
    synchronize: true,
    entities: [
        Word_1.Word,
        SavedWord_1.SavedWord,
        User_1.User,
        UserRefreshToken_1.UserRefreshToken,
        Topic_1.Topic,
        Pronunciation_1.Pronunciation,
        Notification_1.Notification,
        Language_1.Language,
        GroupChat_1.GroupChat,
        Definition_1.Definition,
        ChatMessage_1.ChatMessage,
        ChatImage_1.ChatImage,
    ],
})
    .then((connection) => __awaiter(this, void 0, void 0, function* () {
    const app = express_1.default();
    app.use(express_1.default.json());
    app.use("/auth", auth_1.default);
    app.use("/word", word_1.default);
    app.listen(4000, () => {
        console.log("Server is running on port 4000");
    });
}))
    .catch((error) => console.log(error));
//# sourceMappingURL=index.js.map