import { Request, Response } from "express";
import { getOneUser } from "../controllers/UserController";
import ApiResponse from "./ApiResponse";

export const checkAuthentication = (
  req: Request,
  res: Response,
  next: Function
) => {
  const authHeader = req.headers["authorization"];
  let token = authHeader && authHeader.split(" ")[1];

  if (!token) {
    res.send(new ApiResponse(false, "Not logged in!", null));
  } else {
    jwt.verify(
      token,
      process.env.ACCESS_TOKEN_SECRET!,
      async (err: any, decoded: any) => {
        if (err) {
          res.send(new ApiResponse(false, "Something went wrong!", null));
        } else {
          (req as any).user = await getOneUser(decoded.user.email);

          next();
        }
      }
    );
  }
};
