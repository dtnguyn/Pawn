import dotenv from "dotenv";
dotenv.config();
import { Router } from "express";
import CustomError from "../utils/CustomError";
import {
  getFeedDetail,
  getFeeds,
  updateTopics,
} from "../controllers/FeedController";
import { getSavedWords } from "../controllers/WordController";
import { User } from "../entity/User";
import ApiResponse from "../utils/ApiResponse";
import { checkAuthentication } from "../utils/middlewares";

const router = Router();

router.get("/", checkAuthentication, async (req, res) => {
  try {
    const userId = (req as any).user.id as string;
    if (!userId) {
      throw new CustomError("Please login first!");
    }

    var feedTopics = (req as any).user.feedTopics as string;

    if (feedTopics == undefined || feedTopics == null) {
      feedTopics = "";
    }

    const language = req.query.language as string;

    if (!language) {
      throw new CustomError("Please provide target language!");
    }
    const words = await getSavedWords(userId, language);

    const feeds = await getFeeds(words, language, feedTopics);
    console.log("feeds length ", feeds.length);

    res.send(new ApiResponse(true, "", feeds));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

router.get("/topics", checkAuthentication, async (req, res) => {
  try {
    const user = (req as any).user as User;
    if (!user.id) {
      throw new CustomError("Please login first!");
    }

    res.send(new ApiResponse(true, "", user.feedTopics));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

router.post("/topics", checkAuthentication, async (req, res) => {
  try {
    const userId = (req as any).user.id as string;
    if (!userId) {
      throw new CustomError("Please login first!");
    }

    const newTopicsString = req.body.newTopics;

    if (!newTopicsString) {
      throw new CustomError("Invalid input for updating topics!");
    }

    await updateTopics(userId, newTopicsString);
    res.send(new ApiResponse(true, "", newTopicsString));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

router.get("/detail", checkAuthentication, async (req, res) => {
  try {
    const feedType = req.query.type as string;
    if (!feedType) {
      throw new CustomError("Please provide feed type!");
    }

    const feedUrl = req.query.url as string;
    if (!feedUrl) {
      throw new CustomError("Please provide feed url!");
    }

    const feedId = req.query.id as string;
    if (!feedUrl) {
      throw new CustomError("Please provide feed id!");
    }

    const feedDetail = await getFeedDetail(feedId, feedType, feedUrl);

    res.send(new ApiResponse(true, "", feedDetail));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

export default router;
