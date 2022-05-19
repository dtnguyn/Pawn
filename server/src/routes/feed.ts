import dotenv from "dotenv";
dotenv.config();
import { Router } from "express";
import CustomError from "../utils/CustomError";
import {
  getFeedDetail,
  getFeeds,
  getVideoSubtitle,
  updateTopics,
} from "../controllers/FeedController";
import {
  getSavedWords,
  getWordDetailSimplify,
} from "../controllers/WordController";
import { User } from "../entity/User";
import ApiResponse from "../utils/ApiResponse";
import { checkAuthentication } from "../utils/middlewares";

const router = Router();

router.get("/", checkAuthentication, async (req, res) => {
  try {
    const user = (req as any).user;
    if (!user) {
      throw new CustomError("Please login first!");
    }

    var feedTopics = (req as any).user.feedTopics as string;

    if (feedTopics == undefined || feedTopics == null || !user.isPremium) {
      feedTopics = "";
    }

    const language = req.query.language as string;

    if (!language) {
      throw new CustomError("Please provide target language!");
    }
    const words = await getSavedWords(user.id, language);

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

    console.log("update topic sting", newTopicsString);

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

router.get("/word/definition", checkAuthentication, async (req, res) => {
  try {
    const wordValue = req.query.word as string;

    if (!wordValue) {
      throw new CustomError("Please provide word value!");
    }

    const language = req.query.language as string;
    if (!language) {
      throw new CustomError("Please provide target language!");
    }

    const word = await getWordDetailSimplify(wordValue, language);
    console.log(word, wordValue, language);

    res.send(new ApiResponse(true, "", word));
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get feed error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

router.get("/video/subtitle", checkAuthentication, async (req, res) => {
  try {
    const user = (req as any).user;
    if (!user) {
      throw new CustomError("Please login first!");
    }

    const videoId = req.query.videoId as string;

    if (!videoId) {
      throw new CustomError("Please provide the id of the video!");
    }

    const language = req.query.language as string;

    if (!language) {
      throw new CustomError("Please provide the target language!");
    }

    const translatedLanguage = req.query.translatedLanguage as string;
    console.log(translatedLanguage);

    if (!translatedLanguage) {
      throw new CustomError("Please provide the translated language!");
    }

    const subtitleParts = await getVideoSubtitle(
      videoId,
      language,
      translatedLanguage
    );

    if (subtitleParts) res.send(new ApiResponse(true, "", subtitleParts));
    else throw new CustomError("Couldn't find subtitle for this video!");
  } catch (error) {
    if (error instanceof CustomError) {
      res.send(new ApiResponse(false, error.message, null));
    } else {
      console.log("get subtitle error ", error.message);
      res.send(new ApiResponse(false, "Something went wrong", null));
    }
  }
});

export default router;
