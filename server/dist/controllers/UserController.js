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
const UserRefreshToken_1 = require("../entity/UserRefreshToken");
const typeorm_1 = require("typeorm");
const User_1 = require("../entity/User");
const VerificationCode_1 = require("../entity/VerificationCode");
const nodemailer_1 = __importDefault(require("nodemailer"));
const CustomError_1 = __importDefault(require("../utils/CustomError"));
function createUser(username, nativeLanguageId, email, password, avatar) {
    return __awaiter(this, void 0, void 0, function* () {
        const userRepo = typeorm_1.getRepository(User_1.User);
        yield userRepo.insert({
            username,
            email,
            password,
            nativeLanguageId,
            appLanguageId: nativeLanguageId,
            avatar,
        });
    });
}
exports.createUser = createUser;
function createOauthUser(oauthId, username, email, avatar) {
    return __awaiter(this, void 0, void 0, function* () {
        const userRepo = typeorm_1.getRepository(User_1.User);
        yield userRepo.insert({
            oauthId,
            email,
            username,
            avatar,
        });
    });
}
exports.createOauthUser = createOauthUser;
function getOneUser(usernameOrEmail) {
    return __awaiter(this, void 0, void 0, function* () {
        const userRepo = typeorm_1.getRepository(User_1.User);
        return yield userRepo
            .createQueryBuilder("user")
            .leftJoinAndSelect("user.learningLanguages", "learningLanguages")
            .where("user.username = :username", { username: usernameOrEmail })
            .orWhere("user.email = :email", { email: usernameOrEmail })
            .getOne();
    });
}
exports.getOneUser = getOneUser;
function getOneOauthUser(oauthId) {
    return __awaiter(this, void 0, void 0, function* () {
        const userRepo = typeorm_1.getRepository(User_1.User);
        return yield userRepo.findOne({ oauthId });
    });
}
exports.getOneOauthUser = getOneOauthUser;
function saveRefreshToken(userId, refreshToken) {
    return __awaiter(this, void 0, void 0, function* () {
        const userTokenRepo = typeorm_1.getRepository(UserRefreshToken_1.UserRefreshToken);
        yield userTokenRepo.insert({ userId, token: refreshToken });
    });
}
exports.saveRefreshToken = saveRefreshToken;
function findOneRefreshToken(token) {
    return __awaiter(this, void 0, void 0, function* () {
        const userTokenRepo = typeorm_1.getRepository(UserRefreshToken_1.UserRefreshToken);
        return yield userTokenRepo.findOne({ token });
    });
}
exports.findOneRefreshToken = findOneRefreshToken;
exports.verifyCode = (email, code) => __awaiter(this, void 0, void 0, function* () {
    const verifyRepo = typeorm_1.getRepository(VerificationCode_1.VerificationCode);
    if (yield verifyRepo.findOne({ email, code })) {
        yield verifyRepo.delete({ email });
        return true;
    }
    else {
        return false;
    }
});
exports.sendVerificationCode = (email) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    if (email && (yield userRepo.findOne({ email }))) {
        const code = Math.floor(100000 + Math.random() * 900000).toString();
        const verifyRepo = typeorm_1.getRepository(VerificationCode_1.VerificationCode);
        if (yield verifyRepo.findOne({ email })) {
            yield verifyRepo.update({ email }, { code });
        }
        else {
            yield verifyRepo.insert({ email, code });
        }
        let testAccount = yield nodemailer_1.default.createTestAccount();
        const transporter = nodemailer_1.default.createTransport({
            host: "smtp.ethereal.email",
            port: 587,
            secure: false,
            auth: {
                user: testAccount.user,
                pass: testAccount.pass,
            },
        });
        let info = yield transporter.sendMail({
            from: testAccount.user,
            to: email,
            subject: "Verification code",
            text: `Please use this code to verify your account: ${code}`,
        });
        console.log("Message sent to: ", email);
        console.log("Preview URL: %s", nodemailer_1.default.getTestMessageUrl(info));
        return code;
    }
    else {
        throw new CustomError_1.default("Please provide a valid email!");
    }
});
exports.deleteRefreshToken = (refreshToken) => __awaiter(this, void 0, void 0, function* () {
    try {
        yield typeorm_1.getRepository(UserRefreshToken_1.UserRefreshToken).delete({ token: refreshToken });
    }
    catch (error) {
        throw new CustomError_1.default("Unable to logout!");
    }
});
exports.changePassword = (email, code, hashPW) => __awaiter(this, void 0, void 0, function* () {
    if (yield exports.verifyCode(email, code)) {
        const userRepo = typeorm_1.getRepository(User_1.User);
        yield userRepo.update({ email }, { password: hashPW });
    }
    else
        throw new CustomError_1.default("Invalid verification code!");
});
exports.updateUser = (userId, username, email, avatar, dailyWordCount, notificationEnabled, nativeLanguageId, appLanguageId) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    console.log("app: ", appLanguageId);
    yield userRepo.update({ id: userId }, {
        username,
        email,
        avatar,
        dailyWordCount,
        notificationEnabled,
        nativeLanguageId,
        appLanguageId,
    });
    const newUser = yield userRepo.findOne({ id: userId });
    return newUser;
});
//# sourceMappingURL=UserController.js.map