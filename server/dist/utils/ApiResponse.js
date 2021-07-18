"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class ApiResponse {
    constructor(status, message, data) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}
exports.default = ApiResponse;
//# sourceMappingURL=ApiResponse.js.map