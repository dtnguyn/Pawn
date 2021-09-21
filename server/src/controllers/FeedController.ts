import { WordDetailSimplifyJSON } from "src/utils/types";
import superagent from "superagent";
import { FeedJSON } from "../utils/types";

export const getFeeds = async (
  savedWords: WordDetailSimplifyJSON[],
  language: string
) => {
  const queryValues = savedWords.map((savedWord) => savedWord.value);

  const newsFeeds = await getNews(queryValues, language);
  const videoFeeds = await getVideos(queryValues, language);

  const feeds = newsFeeds.concat(videoFeeds);
  shuffle(feeds);

  return feeds;
};

const getNews = async (queryValues: string[], language: string) => {
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
};

const getVideos = async (queryValues: string[], language: string) => {
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
