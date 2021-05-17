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
const UserController_1 = require("../controllers/UserController");
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const bcrypt_1 = __importDefault(require("bcrypt"));
const express_2 = __importDefault(require("express"));
const router = express_1.Router();
router.use(express_2.default.json());
const checkAuthentication = (req, res, next) => {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
    if (token == null)
        return res.sendStatus(401);
    jsonwebtoken_1.default.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, decoded) => {
        console.log(err);
        if (err) {
            res.sendStatus(403);
        }
        else {
            req.user = decoded.user;
            next();
        }
    });
};
router.get("/", checkAuthentication, (req, res) => {
    const user = req && req.user;
    if (user) {
        console.log(user);
        res.json(user);
    }
    else {
        res.sendStatus(404);
    }
});
router.post("/register", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const username = req.body.username;
        const email = req.body.email;
        const password = req.body.password;
        const avatar = req.body.avatar;
        const native = req.body.nativeLanguage;
        console.log(req.body);
        if (!username || !email || !password || !native)
            throw new Error("Please provide the required information");
        const hashPW = bcrypt_1.default.hashSync(password, parseInt(process.env.SALT_ROUNDS));
        yield UserController_1.createUser(username, native, email, hashPW, avatar);
        return res.json(true);
    }
    catch (error) {
        return res.status(400).send({
            message: error.message,
        });
    }
}));
router.post("/login", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        if (!req.body.usernameOrEmail)
            throw new Error("Please provide username or email!");
        if (!req.body.password)
            throw new Error("Please provide password");
        const user = yield UserController_1.getOneUser(req.body.usernameOrEmail);
        if (!user)
            throw new Error("No user found with the given username or password!");
        const result = yield bcrypt_1.default.compare(req.body.password, user.password);
        if (!result)
            throw new Error("Invalid credentials!");
        const accessToken = jsonwebtoken_1.default.sign({ user }, process.env.ACCESS_TOKEN_SECRET, {
            expiresIn: "30s",
        });
        const refreshToken = jsonwebtoken_1.default.sign({ user }, process.env.REFRESH_TOKEN_SECRET);
        yield UserController_1.saveRefreshToken(user.id, refreshToken);
        return res.json({ accessToken, refreshToken });
    }
    catch (error) {
        return res.status(400).send({
            message: error.message,
        });
    }
}));
router.post("/token", (req, res) => {
    const refreshToken = req.body.token;
    if (refreshToken == null)
        return res.sendStatus(401);
    if (!UserController_1.findOneRefreshToken(refreshToken))
        return res.sendStatus(403);
    jsonwebtoken_1.default.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, user) => {
        if (err)
            return res.sendStatus(403);
        else {
            const accessToken = jsonwebtoken_1.default.sign({ user }, process.env.ACCESS_TOKEN_SECRET, {
                expiresIn: "30s",
            });
            res.json({ accessToken: accessToken });
        }
    });
});
exports.default = router;
//# sourceMappingURL=auth.js.map