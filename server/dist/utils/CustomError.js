"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class CustomError extends Error {
    constructor(msg) {
        super(msg);
        Object.setPrototypeOf(this, CustomError.prototype);
    }
}
exports.default = CustomError;
//# sourceMappingURL=CustomError.js.map