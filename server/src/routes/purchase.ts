import { Router } from "express";
import { executePurchase } from "../controllers/PurchaseController";
import ApiResponse from "../utils/ApiResponse";
import CustomError from "../utils/CustomError";
import { checkAuthentication } from "../utils/middlewares";

const router = Router();

router.post("/", checkAuthentication, async (req, res) => {
  try {
    console.log("purchasing premium...");
    const userId = (req.user as any).id as string;
    const token = req.body.purchaseToken;
    const orderId = req.body.orderId;
    const purchaseTime = req.body.purchaseTime;

    console.log("purchase params", userId, token, orderId, purchaseTime);

    if (!userId) {
      throw new CustomError("Please log in first!");
    }

    if (!token) {
      throw new CustomError("Please provide purchase token!");
    }

    if (!orderId) {
      throw new CustomError("Please provide purchase order id!");
    }

    if (!purchaseTime) {
      throw new CustomError("Please provide purchase time!");
    }

    const result = await executePurchase(token, purchaseTime, orderId, userId);

    console.log("Purchase premium result", result);

    return res.send(new ApiResponse(true, "", result));
  } catch (error) {
    if (error instanceof CustomError) {
      return res.send(new ApiResponse(false, error.message, null));
    } else {
      return res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

export default router;
