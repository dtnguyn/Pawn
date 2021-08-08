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
const UserController_1 = require("../controllers/UserController");
const ApiResponse_1 = __importDefault(require("./ApiResponse"));
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
exports.checkAuthentication = (req, res, next) => {
    const authHeader = req.headers["authorization"];
    let token = authHeader && authHeader.split(" ")[1];
    if (!token) {
        res.send(new ApiResponse_1.default(false, "Not logged in!", null));
    }
    else {
        jsonwebtoken_1.default.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, decoded) => __awaiter(this, void 0, void 0, function* () {
            if (err) {
                res.send(new ApiResponse_1.default(false, "Something went wrong!", null));
            }
            else {
                req.user = yield UserController_1.getOneUser(decoded.user.email);
                next();
            }
        }));
    }
};
//# sourceMappingURL=middlewares.js.map