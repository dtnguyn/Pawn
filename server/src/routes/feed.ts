import dotenv from "dotenv";
dotenv.config();
import { Router } from "express";
import CustomError from "../utils/CustomError";
import { getFeeds } from "../controllers/FeedController";
import { getSavedWords } from "../controllers/WordController";
import { User } from "../entity/User";
import ApiResponse from "../utils/ApiResponse";
import { checkAuthentication } from "../utils/middlewares";

const router = Router();

router.get("/", checkAuthentication, async (req, res) => {
  try {
    const userId = (req as any).user.id;
    if (!userId) {
      throw new CustomError("Please login first!");
    }
    const language = req.query.language as string;

    if (!language) {
      throw new CustomError("Please provide target language!");
    }
    const words = await getSavedWords(userId, language);

    const feeds = await getFeeds(words, language);
    console.log("feeds length ", feeds.length)

    res.send(new ApiResponse(true, "", feeds));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message)
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

export default router;
