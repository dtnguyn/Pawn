import express from "express";
import "reflect-metadata";
import { createConnection, getRepository } from "typeorm";
import { ChatImage } from "./entity/ChatImage";
import { ChatMessage } from "./entity/ChatMessage";
import { Definition } from "./entity/Definition";
import { GroupChat } from "./entity/GroupChat";
import { Language } from "./entity/Language";
import { Notification } from "./entity/Notification";
import { Pronunciation } from "./entity/Pronunciation";
import { SavedWord } from "./entity/SavedWord";
import { Topic } from "./entity/Topic";
import { User } from "./entity/User";
import { UserRefreshToken } from "./entity/UserRefreshToken";
import { VerificationCode } from "./entity/VerificationCode";
import { Word } from "./entity/Word";
import auth from "./routes/auth";
import word from "./routes/word";
import language from "./routes/language";
import { importAllWords } from "./controllers/WordController";
import { DailyWord } from "./entity/DailyWord";

createConnection({
  type: "postgres",
  host: "localhost",
  port: 5432,
  username: "postgres",
  password: "postgres",
  database: "pawn_dev",
  synchronize: true,
  entities: [
    Word,
    SavedWord,
    DailyWord,
    User,
    UserRefreshToken,
    Topic,
    Pronunciation,
    Notification,
    Language,
    GroupChat,
    Definition,
    ChatMessage,
    ChatImage,
    VerificationCode,
  ],
})
  .then(async (_) => {
    const app = express();
    app.use(express.json());

    app.use("/auth", auth);
    app.use("/word", word);
    app.use("/language", language);

    // await importAllWords();
    // console.log(await getRepository(User).delete({ email: "test@test.com" }));
    // await importAllLanguages();

    // console.log(
    //   await getRepository(Language)
    //     .createQueryBuilder("language")
    //     .leftJoinAndSelect("language.learners", "learner")
    //     .getMany()
    // );
    // console.log(await getRepository(Language).find());

    // console.log(await getRepository(User).find());

    app.listen(4000, () => {
      console.log("Server is running on port 4000");
    });
  })
  .catch((error) => console.log(error));
