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
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (Object.hasOwnProperty.call(mod, k)) result[k] = mod[k];
    result["default"] = mod;
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
const bcrypt_1 = __importDefault(require("bcrypt"));
const dotenv_1 = __importDefault(require("dotenv"));
const express_1 = __importStar(require("express"));
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const passport_1 = __importDefault(require("passport"));
const passport_google_oauth20_1 = require("passport-google-oauth20");
const middlewares_1 = require("../utils/middlewares");
const UserController_1 = require("../controllers/UserController");
const ApiResponse_1 = __importDefault(require("../utils/ApiResponse"));
const CustomError_1 = __importDefault(require("../utils/CustomError"));
dotenv_1.default.config();
const router = express_1.Router();
router.use(express_1.default.json());
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
router.get("/", middlewares_1.checkAuthentication, (req, res) => {
    const user = req && req.user;
    if (user) {
        res.send(new ApiResponse_1.default(true, "", user));
    }
    else {
        res.send(new ApiResponse_1.default(true, "Not logged in!", null));
    }
});
router.get("/verify/code", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const code = yield UserController_1.sendVerificationCode(email);
        res.send(new ApiResponse_1.default(true, "", code));
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
router.post("/verify/code", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const code = req.body.code;
        const result = yield UserController_1.verifyCode(email, code);
        res.send(new ApiResponse_1.default(true, "", code));
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
router.post("/register", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const username = req.body.username;
        const email = req.body.email;
        const password = req.body.password;
        const avatar = req.body.avatar;
        const native = req.body.nativeLanguage;
        if (!username || !email || !password || !native)
            throw new CustomError_1.default("Please provide the required information");
        const hashPW = bcrypt_1.default.hashSync(password, parseInt(process.env.SALT_ROUNDS));
        yield UserController_1.createUser(username, native, email, hashPW, avatar);
        return res.send(new ApiResponse_1.default(true, "", null));
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
router.post("/login", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        if (!req.body.usernameOrEmail)
            throw new CustomError_1.default("Please provide username or email!");
        if (!req.body.password)
            throw new CustomError_1.default("Please provide password");
        const user = yield UserController_1.getOneUser(req.body.usernameOrEmail);
        if (!user)
            throw new CustomError_1.default("No user found with the given username or password!");
        const result = yield bcrypt_1.default.compare(req.body.password, user.password);
        if (!result)
            throw new CustomError_1.default("Invalid credentials!");
        const accessToken = jsonwebtoken_1.default.sign({ user }, process.env.ACCESS_TOKEN_SECRET, {
            expiresIn: process.env.TOKEN_EXPIRATION,
        });
        const refreshToken = jsonwebtoken_1.default.sign({ user }, process.env.REFRESH_TOKEN_SECRET);
        yield UserController_1.saveRefreshToken(user.id, refreshToken);
        return res.send(new ApiResponse_1.default(true, "", { accessToken, refreshToken }));
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
router.delete("/logout", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const refreshToken = req.body.refreshToken;
        yield UserController_1.deleteRefreshToken(refreshToken);
        return res.send(new ApiResponse_1.default(true, "", null));
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
router.patch("/password", (req, res) => __awaiter(this, void 0, void 0, function* () {
    try {
        const email = req.body.email;
        const newPassword = req.body.newPassword;
        const code = req.body.code;
        const hashPW = bcrypt_1.default.hashSync(newPassword, parseInt(process.env.SALT_ROUNDS));
        yield UserController_1.changePassword(email, code, hashPW);
        return res.send(new ApiResponse_1.default(true, "", null));
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
router.post("/token", (req, res) => {
    try {
        const refreshToken = req.body.token;
        if (refreshToken == null)
            throw new CustomError_1.default("Unauthorized");
        if (!UserController_1.findOneRefreshToken(refreshToken))
            throw new CustomError_1.default("Forbidden");
        jsonwebtoken_1.default.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, decoded) => {
            if (err)
                throw err;
            else {
                const accessToken = jsonwebtoken_1.default.sign({ user: decoded.user }, process.env.ACCESS_TOKEN_SECRET, {
                    expiresIn: process.env.TOKEN_EXPIRATION,
                });
                return res.send(new ApiResponse_1.default(true, "", { accessToken: accessToken }));
            }
        });
    }
    catch (error) {
        if (error instanceof CustomError_1.default) {
            res.send(new ApiResponse_1.default(false, error.message, null));
        }
        else {
            res.send(new ApiResponse_1.default(false, "Something went wrong", null));
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