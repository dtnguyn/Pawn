import { User } from "../entity/User";
import {
  FeedDetailJSON,
  NewsDetailJSON,
  WordDetailSimplifyJSON,
} from "../utils/types";
import superagent from "superagent";
import { getRepository } from "typeorm";
import { FeedJSON } from "../utils/types";
import TinyURL from "tinyurl";

// import Mercury from "@postlight/mercury-parser";

export const getFeeds = async (
  savedWords: WordDetailSimplifyJSON[],
  language: string,
  feedTopics: string
) => {
  const queryValues = savedWords.map((savedWord) => savedWord.value);

  const newsFeeds = await getNews(queryValues, language, feedTopics);
  const videoFeeds = await getVideos(queryValues, language, feedTopics);

  const feeds = newsFeeds.concat(videoFeeds);
  shuffle(feeds);

  return feeds;
};

export const getFeedDetail = async (
  id: string,
  feedType: string,
  feedUrl: string
) => {
  if (feedType === "news") {
    const detail = await getNewsDetail(id, feedUrl);
    return detail;
  } else {
    //Get video detail
    return null;
  }
};

export const getVideoSubtitle = async (videoId: string) => {
  const result = await superagent
    .get(`https://subtitles-for-youtube.p.rapidapi.com/subtitles/${videoId}`)
    .set("x-rapidapi-host", "subtitles-for-youtube.p.rapidapi.com")
    .set("x-rapidapi-key", process.env.SUBTITLE_API_KEY as string);

  const subtitle = result.body;
  console.log("result", result.body);
  return subtitle ? subtitle : null;
};

const getNewsDetail = async (id: string, newsUrl: string) => {
  const shortenUrl = (await TinyURL.shorten(newsUrl)) as string;

  const result = await superagent
    .get(`https://article-parser.p.rapidapi.com/`)
    .query({ website_url: shortenUrl })
    .set("x-rapidapi-host", "article-parser.p.rapidapi.com")
    .set("x-rapidapi-key", process.env.NEWS_PARSER_API_KEY as string);

  return {
    id,
    type: "news",
    title: result.body.title,
    thumbnail: result.body.thumbnail,
    content: {
      value: result.body.text
        ? result.body.text
        : "Cannot load data for this article!",
      images: result.body.images,
    } as NewsDetailJSON,
  } as FeedDetailJSON;
};

const getNews = async (
  queryValues: string[],
  language: string,
  topics: string
) => {
  try {
    let queryString = "";
    queryValues.forEach((query, index) => {
      if (index == queryValues.length - 1) queryString += query;
      else queryString += query + " OR ";
    });

    const result = await superagent
      .get(`https://free-news.p.rapidapi.com/v1/search`)
      .query({ q: queryString, lang: language.substring(0, 2) })
      .set("x-rapidapi-host", "free-news.p.rapidapi.com")
      .set("x-rapidapi-key", process.env.NEWS_API_KEY as string);

    const articles: any[] = result.body.articles;

    if (articles) {
      const newsFeeds = articles.map((article) => {
        if (article._id && article.title && article.summary && article.link) {
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
            publishedDate: article.published_date,
          } as FeedJSON;
        }
      });

      return newsFeeds.filter((news) => news != undefined);
    } else return [];
  } catch (error) {
    console.log("Error when getting news feed: ", error.message);
    return [];
  }
};

const getVideos = async (
  queryValues: string[],
  language: string,
  topics: string
) => {
  try {
    let queryString = "";
    queryValues.forEach((query, index) => {
      if (index == queryValues.length - 1) queryString += query;
      else queryString += query + "|";
    });

    const result = await superagent
      .get("https://youtube.googleapis.com/youtube/v3/search")
      .query({
        key: process.env.YOUTUBE_API_KEY,
        part: "snippet",
        q: queryString,
        relevanceLanguage: language,
        videoCaption: "closedCaption",
        type: "video",
      });

    const videos = result.body.items as any[];
    if (videos) {
      const videoFeeds = videos.map((video) => {
        if (video.id && video.id.videoId && video.snippet) {
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
            publishedDate: video.snippet.publishedAt.toString(),
          } as FeedJSON;
        }
      });

      return videoFeeds.filter((video) => video != undefined);
    } else return [];
  } catch (error) {
    console.log("Error when get videos feed: ", error.message);
    return [];
  }
};

function shuffle(array: any[]) {
  let currentIndex = array.length,
    randomIndex;

  // While there remain elements to shuffle...
  while (currentIndex != 0) {
    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex--;

    // And swap it with the current element.
    [array[currentIndex], array[randomIndex]] = [
      array[randomIndex],
      array[currentIndex],
    ];
  }

  return array;
}

export const updateTopics = async (userId: string, newTopicString: string) => {
  const userRepo = getRepository(User);

  await userRepo.update({ id: userId }, { feedTopics: newTopicString });
};
