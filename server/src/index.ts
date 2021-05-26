import "reflect-metadata";
import { createConnection, getRepository } from "typeorm";
import { ChatImage } from "./entity/ChatImage";
import { ChatMessage } from "./entity/ChatMessage";
import { Definition } from "./entity/Definition";
import { GroupChat } from "./entity/GroupChat";
import { Language } from "./entity/Language";
import { Notification } from "./entity/Notification";
import { Pronunciation } from "./entity/Pronunciation";
import { Topic } from "./entity/Topic";
import { User } from "./entity/User";
import { Word } from "./entity/Word";
import express from "express";
import auth from "./routes/auth";
import word from "./routes/word";

import { UserRefreshToken } from "./entity/UserRefreshToken";
import { SavedWord } from "./entity/SavedWord";
import { importAllWords } from "./controllers/WordController";

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
  ],
})
  .then(async (connection) => {
    const app = express();
    app.use(express.json());

    app.use("/auth", auth);
    app.use("/word", word);

    // await importAllWords();
    console.log(await getRepository(Definition).find());

    app.listen(4000, () => {
      console.log("Server is running on port 4000");
    });
  })
  .catch((error) => console.log(error));
