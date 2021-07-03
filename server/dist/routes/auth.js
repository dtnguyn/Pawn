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
const passport_1 = __importDefault(require("passport"));
const passport_google_oauth20_1 = require("passport-google-oauth20");
const router = express_1.Router();
router.use(express_2.default.json());
const handleAuth = (_accessToken, _refreshToken, profile, cb) => __awaiter(this, void 0, void 0, function* () {
    try {
        const { id, emails, displayName, photos } = profile;
        console.log(id, emails, displayName, photos, profile);
        let user = yield UserController_1.getOneOauthUser(id);
        if (!user) {
            yield UserController_1.createOauthUser(id, displayName, emails && emails[0].value, photos && photos[0].value);
            user = yield UserController_1.getOneOauthUser(id);
        }
        if (user)
            return cb(null, user);
        else
            throw new Error("There's something wrong");
    }
    catch (error) {
        return cb(error, undefined);
    }
});
router.use(passport_1.default.initialize());
passport_1.default.use(new passport_google_oauth20_1.Strategy({
    clientID: process.env.GOOGLE_CLIENT_ID,
    clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    callbackURL: process.env.GOOGLE_CLIENT_SECRET,
}, handleAuth));
exports.checkAuthentication = (req, res, next) => {
    const authHeader = req.headers["authorization"];
    let token = authHeader && authHeader.split(" ")[1];
    if (!token)
        res.sendStatus(401);
    else {
        jsonwebtoken_1.default.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, decoded) => __awaiter(this, void 0, void 0, function* () {
            if (err) {
                res.sendStatus(403);
            }
            else {
                req.user = yield UserController_1.getOneUser(decoded.user.email);
                next();
            }
        }));
    }
};
router.get("/", exports.checkAuthentication, (req, res) => {
    const user = req && req.user;
    if (user) {
        res.json(user);
        console.log(user);
    }
    else {
        res.sendStatus(404);
    }
});
router.get("/verify/code", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const code = yield UserController_1.sendVerificationCode(email);
        res.json(code);
    }
    catch (error) {
        res.status(400).send({
            message: error.message,
        });
    }
}));
router.post("/verify/code", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const code = req.body.code;
        const result = yield UserController_1.verifyCode(email, code);
        res.json(result);
    }
    catch (error) {
        res.status(400).send({
            message: error.message,
        });
    }
}));
router.post("/register", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const username = req.body.username;
        const email = req.body.email;
        const password = req.body.password;
        const avatar = req.body.avatar;
        const native = req.body.nativeLanguage;
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
            expiresIn: process.env.TOKEN_EXPIRATION,
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
router.delete("/logout", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const refreshToken = req.body.refreshToken;
        yield UserController_1.deleteRefreshToken(refreshToken);
        res.json(true);
    }
    catch (error) {
        return res.status(400).send({
            message: error.message,
        });
    }
}));
router.patch("/password", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const newPassword = req.body.newPassword;
        const code = req.body.code;
        const hashPW = bcrypt_1.default.hashSync(newPassword, parseInt(process.env.SALT_ROUNDS));
        yield UserController_1.changePassword(email, code, hashPW);
        res.json(true);
    }
    catch (error) {
        res.status(400).send({
            message: error.message,
        });
    }
}));
router.post("/token", (req, res) => {
    try {
        const refreshToken = req.body.token;
        if (refreshToken == null)
            throw new Error("Unauthorized");
        if (!UserController_1.findOneRefreshToken(refreshToken))
            throw new Error("Forbidden");
        jsonwebtoken_1.default.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, decoded) => {
            if (err)
                res.sendStatus(403);
            else {
                const accessToken = jsonwebtoken_1.default.sign({ user: decoded.user }, process.env.ACCESS_TOKEN_SECRET, {
                    expiresIn: process.env.TOKEN_EXPIRATION,
                });
                res.json({ accessToken: accessToken });
            }
        });
    }
    catch (error) {
        if (error.message === "Unauthorized") {
            res.sendStatus(401);
        }
        else if (error.message === "Forbidden") {
            res.sendStatus(403);
        }
        else {
            res.sendStatus(400);
        }
    }
});
router.get("/google", passport_1.default.authenticate("google", { scope: ["profile", "email"] }));
router.get("/google/callback", passport_1.default.authenticate("google", { session: false }), (req, res) => __awaiter(this, void 0, void 0, function* () {
    const accessToken = jsonwebtoken_1.default.sign({ user: req.user }, process.env.ACCESS_TOKEN_SECRET, {
        expiresIn: process.env.TOKEN_EXPIRATION,
    });
    const refreshToken = jsonwebtoken_1.default.sign({ user: req.user }, process.env.REFRESH_TOKEN_SECRET);
    yield UserController_1.saveRefreshToken(req.user.id, refreshToken);
    res.json({
        accessToken: accessToken,
        refreshToken: refreshToken,
    });
}));
exports.default = router;
//# sourceMappingURL=auth.js.map