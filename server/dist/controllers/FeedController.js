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
const User_1 = require("../entity/User");
const superagent_1 = __importDefault(require("superagent"));
const typeorm_1 = require("typeorm");
const unfluff_1 = __importDefault(require("unfluff"));
const moment_1 = __importDefault(require("moment"));
exports.getFeeds = (savedWords, language, feedTopics) => __awaiter(this, void 0, void 0, function* () {
    const queryValues = savedWords.map((savedWord) => savedWord.value);
    const newsFeeds = yield getNews(queryValues, language, feedTopics);
    const videoFeeds = yield getVideos(queryValues, language, feedTopics);
    const feeds = newsFeeds.concat(videoFeeds);
    shuffle(feeds);
    return feeds;
});
exports.getFeedDetail = (id, feedType, feedUrl) => __awaiter(this, void 0, void 0, function* () {
    if (feedType === "news") {
        const detail = yield getNewsDetail(id, feedUrl);
        return detail;
    }
    else {
        return null;
    }
});
exports.getVideoSubtitle = (videoId, lang, translatedLang) => __awaiter(this, void 0, void 0, function* () {
    const orgResponse = superagent_1.default
        .get(`https://subtitles-for-youtube.p.rapidapi.com/subtitles/${videoId}`)
        .query({ lang: lang.substr(0, 2) })
        .set("x-rapidapi-host", "subtitles-for-youtube.p.rapidapi.com")
        .set("x-rapidapi-key", process.env.SUBTITLE_API_KEY);
    const translatedResponse = superagent_1.default
        .get(`https://subtitles-for-youtube.p.rapidapi.com/subtitles/${videoId}`)
        .query({ translated: "Translated", lang: translatedLang.substr(0, 2) })
        .set("x-rapidapi-host", "subtitles-for-youtube.p.rapidapi.com")
        .set("x-rapidapi-key", process.env.SUBTITLE_API_KEY);
    const data = yield Promise.all([orgResponse, translatedResponse]);
    const subtitle = data[0].body;
    const translatedSubtitle = data[1].body;
    var result = null;
    if (subtitle.length && translatedSubtitle.length) {
        result = [];
        for (let i = 0; i < subtitle.length; i++) {
            result.push({
                start: subtitle[i].start,
                end: subtitle[i].end,
                dur: subtitle[i].dur,
                text: subtitle[i].text,
                translatedText: translatedSubtitle[i]
                    ? translatedSubtitle[i].text
                    : "Unable to translate",
                translatedLang: translatedLang.substr(0, 2),
                lang: lang.substr(0, 2),
            });
        }
    }
    console.log(lang.substr(0, 2), subtitle.length);
    console.log(translatedLang.substr(0, 2), translatedSubtitle.length);
    return result;
});
const getNewsDetail = (id, newsUrl) => __awaiter(this, void 0, void 0, function* () {
    const result = yield superagent_1.default.get(newsUrl);
    const data = unfluff_1.default(result.text);
    const date = new Date(data.date);
    const formattedDate = moment_1.default(date).format("ll");
    const newsDetail = {
        value: data.text ? data.text : "Article not available",
        images: [data.image],
        source: data.publisher,
        author: data.author.length ? data.author[0] : null,
        publishedDate: formattedDate,
    };
    console.log(formattedDate);
    const feedDetail = {
        id,
        type: "news",
        title: data.title ? data.title : "Unavailable Data",
        thumbnail: data.image,
        content: newsDetail,
    };
    return feedDetail;
});
const getNews = (queryValues, language, topics) => __awaiter(this, void 0, void 0, function* () {
    try {
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
                if (article._id && article.title && article.summary && article.link) {
                    const date = new Date(article.published_date);
                    const formattedDate = moment_1.default(date).format("ll");
                    return {
                        id: article._id,
                        type: "news",
                        title: article.title,
                        author: article.author,
                        topic: article.topic,
                        language: language,
                        url: article.link,
                        description: article.summary,
                        thumbnail: article.media,
                        publishedDate: formattedDate,
                    };
                }
                return null;
            });
            return newsFeeds.filter((news) => news != null);
        }
        else
            return [];
    }
    catch (error) {
        console.log("Error when getting news feed: ", error.message);
        return [];
    }
});
const getVideos = (queryValues, language, topics) => __awaiter(this, void 0, void 0, function* () {
    try {
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
            videoCaption: "closedCaption",
            type: "video",
        });
        const videos = result.body.items;
        if (videos) {
            const videoFeeds = videos.map((video) => {
                if (video.id && video.id.videoId && video.snippet) {
                    const date = new Date(video.snippet.publishedAt.toString());
                    const formattedDate = moment_1.default(date).format("ll");
                    return {
                        id: video.id.videoId,
                        type: "video",
                        title: video.snippet.title,
                        author: video.snippet.channelTitle,
                        thumbnail: video.snippet.thumbnails.high.url
                            ? video.snippet.thumbnails.high.url
                            : null,
                        topic: null,
                        url: `https://youtu.be/${video.id.videoId}`,
                        language: language,
                        description: video.snippet.description,
                        publishedDate: formattedDate,
                    };
                }
            });
            return videoFeeds.filter((video) => video != undefined);
        }
        else
            return [];
    }
    catch (error) {
        console.log("Error when get videos feed: ", error.message);
        return [];
    }
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
exports.updateTopics = (userId, newTopicString) => __awaiter(this, void 0, void 0, function* () {
    const userRepo = typeorm_1.getRepository(User_1.User);
    yield userRepo.update({ id: userId }, { feedTopics: newTopicString });
});
//# sourceMappingURL=FeedController.js.map