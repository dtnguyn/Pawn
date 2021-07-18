// type ApiResponse<T> = {
//   status: boolean;
//   message: string;
//   data: T;
// };

export default class ApiResponse<T> {
  status: boolean;
  message: string;
  data: T | null;

  constructor(status: boolean, message: string, data: T | null) {
    this.data = data;
    this.message = message;
    this.status = status;
  }
}
