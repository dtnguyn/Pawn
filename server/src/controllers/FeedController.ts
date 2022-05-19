import { User } from "../entity/User";
import {
  FeedDetailJSON,
  NewsDetailJSON,
  SubtitlePart,
  WordDetailSimplifyJSON,
} from "../utils/types";
import superagent from "superagent";
import { getRepository } from "typeorm";
import { FeedJSON } from "../utils/types";
import extractor from "unfluff";
import moment from "moment";

// import Mercury from "@postlight/mercury-parser";

export const getFeeds = async (
  savedWords: WordDetailSimplifyJSON[],
  language: string,
  feedTopics: string
) => {
  const queryValues = savedWords.map((savedWord) => savedWord.value);

  const topicsArr: string[] = feedTopics
    .split(/[ ,]+/)
    .filter((item) => item != "");

  const topicMap = new Map<string, boolean | undefined>();
  for (const topic of topicsArr) {
    topicMap.set(topic, true);
  }

  const videoTopic = topicsArr.length == 0 ? topicsArr[0] : "";

  const newsFeeds = await getNews(queryValues, language, topicMap);
  // const videoFeeds = await getVideos(queryValues, language, videoTopic);
  const videoFeeds = [] as FeedJSON[];

  const feeds = newsFeeds.concat(videoFeeds);
  shuffle(feeds);

  console.log(feeds);

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

export const getVideoSubtitle = async (
  videoId: string,
  lang: string,
  translatedLang: string
) => {
  const orgResponse = superagent
    .get(`https://subtitles-for-youtube.p.rapidapi.com/subtitles/${videoId}`)
    .query({ lang: lang.substr(0, 2) })
    .set("x-rapidapi-host", "subtitles-for-youtube.p.rapidapi.com")
    .set("x-rapidapi-key", process.env.SUBTITLE_API_KEY as string);

  const translatedResponse = superagent
    .get(`https://subtitles-for-youtube.p.rapidapi.com/subtitles/${videoId}`)
    .query({ translated: "Translated", lang: translatedLang.substr(0, 2) })
    .set("x-rapidapi-host", "subtitles-for-youtube.p.rapidapi.com")
    .set("x-rapidapi-key", process.env.SUBTITLE_API_KEY as string);

  const data = await Promise.all([orgResponse, translatedResponse]);
  const subtitle = data[0].body;
  const translatedSubtitle = data[1].body;

  var result: Array<SubtitlePart> | null = null;
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
};

const getNewsDetail = async (id: string, newsUrl: string) => {
  const result = await superagent.get(newsUrl);

  const data = extractor(result.text);

  const date = new Date(data.date);
  const formattedDate = moment(date).format("ll");
  const newsDetail: NewsDetailJSON = {
    value: data.text ? data.text : "Article not available",
    images: [data.image],
    source: data.publisher,
    author: data.author.length ? data.author[0] : null,
    publishedDate: formattedDate,
  };
  console.log(formattedDate);
  const feedDetail: FeedDetailJSON = {
    id,
    type: "news",
    title: data.title ? data.title : "Unavailable Data",
    thumbnail: data.image,
    content: newsDetail,
  };

  return feedDetail;
};

const getNews = async (
  queryValues: string[],
  language: string,
  topicMap: Map<string, boolean | undefined>
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
          const date = new Date(article.published_date);
          const formattedDate = moment(date).format("ll");
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
          } as FeedJSON;
        }
        return null;
      });

      console.log(
        "result",
        newsFeeds.map(
          (item) =>
            `${item.topic} ${
              topicMap.get(item.topic ? item.topic : "null") === true
            }`
        )
      );
      console.log("map", topicMap);

      return newsFeeds.filter(
        (news) => news != null
        // &&
        // topicMap.get(news.topic ? news.topic : "null") === true
      ) as FeedJSON[];
    } else return [];
  } catch (error) {
    console.log("Error when getting news feed: ", error.message);
    return [];
  }
};

const getVideos = async (
  queryValues: string[],
  language: string,
  topic: string
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
        topic,
      });

    const videos = result.body.items as any[];
    if (videos) {
      const videoFeeds = videos.map((video) => {
        if (video.id && video.id.videoId && video.snippet) {
          const date = new Date(video.snippet.publishedAt.toString());
          const formattedDate = moment(date).format("ll");
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
          } as FeedJSON;
        }
      });

      return videoFeeds.filter((video) => video != undefined) as FeedJSON[];
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
