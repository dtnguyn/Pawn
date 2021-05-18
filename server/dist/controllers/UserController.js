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
const UserRefreshToken_1 = require("../entity/UserRefreshToken");
const typeorm_1 = require("typeorm");
const User_1 = require("../entity/User");
function createUser(username, nativeLanguageId, email, password, avatar) {
    return __awaiter(this, void 0, void 0, function* () {
        const userRepo = typeorm_1.getRepository(User_1.User);
        yield userRepo.insert({
            username,
            email,
            password,
            nativeLanguageId,
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
//# sourceMappingURL=UserController.js.map