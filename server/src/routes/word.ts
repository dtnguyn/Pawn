import { Request, Response, Router } from "express";
import ApiResponse from "../utils/ApiResponse";
import {
  getDailyRandomWords,
  getDefinition,
  getSavedWords,
  getWordAutoCompletes,
  rearrangeDefinition,
  rearrangeSavedWords,
  toggleSaveWord,
} from "../controllers/WordController";
import { checkAuthentication } from "../utils/middlewares";

const router = Router();

router.get("/daily", async (req, res) => {
  try {
    let dailyWordCount = 3;
    console.log(req.query);
    if (req.query.dailyWordCount) {
      dailyWordCount = parseInt(req.query.dailyWordCount as string);
    }

    const language = req.query.language as string;
    if (!language)
      return res.send(
        new ApiResponse(false, "Please provide the target language!", null)
      );

    const words = await getDailyRandomWords(dailyWordCount, language);

    return res.send(new ApiResponse(true, "", words));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

router.get("/definition", async (req, res) => {
  try {
    const language = req.query.language as string;
    const word = req.query.word as string;

    if (!word || !language)
      throw new Error("Please provide the word and definition language");

    const definition = await getDefinition(word, language);
    if (!definition)
      throw new Error("Couldn't find definition for the word provided!");
    return res.send(new ApiResponse(true, "", definition));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

router.get("/autocomplete", async (req, res) => {
  try {
    const text = req.query.text as string;
    const language = req.query.language as string;

    if (!text || !language)
      throw new Error("Please provide text and language!");
    const results = await getWordAutoCompletes(language, text);
    return res.send(new ApiResponse(true, "", results));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

router.get("/save", checkAuthentication, async (req, res) => {
  try {
    const userId = (req as any).user.id;
    const language = req.query.language as string;
    const savedWords = await getSavedWords(userId, language);
    return res.send(new ApiResponse(true, "", savedWords));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

//Post routes
router.post("/save", checkAuthentication, async (req, res) => {
  try {
    const word = req.body.word;
    const language = req.body.language;
    const userId = (req as any).user.id;
    await toggleSaveWord(word, language, userId);
    return res.send(new ApiResponse(true, "", null));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

//Patch routes
router.patch("/rearrange", checkAuthentication, async (req, res) => {
  try {
    const wordIds = req.body.wordIds as string[];

    if (!wordIds || !wordIds.length)
      throw new Error("Please provide a list of saved words!");

    await rearrangeSavedWords(wordIds);

    return res.send(new ApiResponse(true, "", null));
  } catch (error) {
    console.log(error);
    return res.send(new ApiResponse(false, error.message, null));
  }
});

router.patch("/definition/rearrange", checkAuthentication, async (req, res) => {
  try {
    const definitionIds = req.body.definitionIds as string[];

    if (!definitionIds || !definitionIds.length)
      throw new Error("Please provide a list of definitions!");

    await rearrangeDefinition(definitionIds);

    return res.send(new ApiResponse(true, "", null));
  } catch (error) {
    return res.send(new ApiResponse(false, error.message, null));
  }
});

export default router;
