import { Request, Response, Router } from "express";
import { getDailyRandomWords } from "../controllers/WordController";
import { User } from "../entity/User";

const router = Router();

const checkLanguage = (language: string) => {
  if (!language) return;
  if (
    language === "en" ||
    language === "de" ||
    language === "fr" ||
    language === "es"
  )
    return true;
  return false;
};

router.get("/daily", async (req, res) => {
  try {
    let dailyWordCount = 3;
    if (req.user) {
      dailyWordCount = (req.user as User).dailyWordCount;
    }

    const language = req.query.language as string;

    const words = await getDailyRandomWords(dailyWordCount, language);

    res.json(words);
  } catch (error) {
    res.status(400).send({
      message: error.message,
    });
  }
});

export default router;
