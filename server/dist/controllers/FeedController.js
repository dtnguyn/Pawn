"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const superagent_1 = __importDefault(require("superagent"));
exports.getFeeds = (savedWords, language) => __awaiter(this, void 0, void 0, function* () {
    const queryValues = savedWords.map((savedWord) => savedWord.value);
    const newsFeeds = yield getNews(queryValues, language);
    const videoFeeds = yield getVideos(queryValues, language);
    const feeds = newsFeeds.concat(videoFeeds);
    shuffle(feeds);
    return feeds;
});
const getNews = (queryValues, language) => __awaiter(this, void 0, void 0, function* () {
    let queryString = "";
    queryValues.forEach((query, index) => {
        if (index == queryValues.length - 1)
            queryString += query;
        else
            queryString += query + " OR ";
    });
    const result = yield superagent_1.default
        .get(`https://free-news.p.rapidapi.com/v1/search`)
        .query({ q: queryString, lang: language.substring(0, 2) })
        .set("x-rapidapi-host", "free-news.p.rapidapi.com")
        .set("x-rapidapi-key", process.env.NEWS_API_KEY);
    const articles = result.body.articles;
    if (articles) {
        const newsFeeds = articles.map((article) => {
            if (article._id && article.title && article.summary) {
                return {
                    id: article._id,
                    type: "news",
                    title: article.title,
                    author: article.author,
                    topic: article.topic,
                    language: article.language,
                    description: article.summary,
                    thumbnail: article.media,
                    publishedDate: article.published_date,
                };
            }
        });
        return newsFeeds.filter((news) => news != undefined);
    }
    else
        return [];
});
const getVideos = (queryValues, language) => __awaiter(this, void 0, void 0, function* () {
    let queryString = "";
    queryValues.forEach((query, index) => {
        if (index == queryValues.length - 1)
            queryString += query;
        else
            queryString += query + "|";
    });
    const result = yield superagent_1.default
        .get("https://youtube.googleapis.com/youtube/v3/search")
        .query({
        key: process.env.YOUTUBE_API_KEY,
        part: "snippet",
        q: queryString,
        relevanceLanguage: language,
        type: "video",
    });
    const videos = result.body.items;
    if (videos) {
        const videoFeeds = videos.map((video) => {
            if (video.id && video.id.videoId && video.snippet) {
                return {
                    id: video.id.videoId,
                    type: "video",
                    title: video.snippet.title,
                    author: video.snippet.channelTitle,
                    thumbnail: video.snippet.thumbnails.high
                        ? video.snippet.thumbnails.high
                        : null,
                    topic: null,
                    language: language,
                    description: video.snippet.description,
                    publishedDate: video.snippet.publishedAt.toString(),
                };
            }
        });
        return videoFeeds.filter((video) => video != undefined);
    }
    else
        return [];
});
function shuffle(array) {
    let currentIndex = array.length, randomIndex;
    while (currentIndex != 0) {
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;
        [array[currentIndex], array[randomIndex]] = [
            array[randomIndex],
            array[currentIndex],
        ];
    }
    return array;
}
//# sourceMappingURL=FeedController.js.map