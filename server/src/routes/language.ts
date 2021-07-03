import { Router } from "express";
import { Language } from "../entity/Language";
import {
  chooseLanguages,
  getLearningLanguages,
} from "../controllers/LanguageController";
import { checkAuthentication } from "./auth";

const router = Router();

//GET ROUTES
router.get("/", checkAuthentication, async (req, res) => {
  try {
    const userId = (req as any).user.id;

    const languages = await getLearningLanguages(userId);
    res.json(new ApiResponse<Language[]>(true, "", languages));
  } catch (error) {
    res.status(400).json(new ApiResponse(false, error.message, null));
  }
});

//POST ROUTES
router.post("/save", checkAuthentication, async (req, res) => {
  try {
    const languages = req.body.languages;
    const userId = (req as any).user.id;

    await chooseLanguages(languages, userId);
    res.json(new ApiResponse(true, "", null));
  } catch (error) {
    res.status(400).json(new ApiResponse(false, error.message, null));
  }
});

export default router;
