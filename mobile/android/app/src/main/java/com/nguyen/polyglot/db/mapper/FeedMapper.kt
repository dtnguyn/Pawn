package com.nguyen.polyglot.db.mapper

import android.annotation.SuppressLint
import com.nguyen.polyglot.db.entity.FeedCacheEntity
import com.nguyen.polyglot.model.Feed
import java.text.SimpleDateFormat
import java.util.*

object FeedMapper {

    @SuppressLint("SimpleDateFormat")
    fun mapToCacheEntity(feed: Feed): FeedCacheEntity {
        return FeedCacheEntity(
            id = feed.id,
            title = feed.title,
            language = feed.language,
            description = feed.description,
            thumbnail = feed.thumbnail,
            author = feed.author,
            topic = feed.topic,
            url = feed.url,
            type = feed.type,
            publishedDate = feed.publishedDate,
            cacheDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
        )
    }

    fun mapToNetworkEntity(feed: FeedCacheEntity): Feed {
        return Feed(
            id = feed.id,
            title = feed.title,
            language = feed.language,
            description = feed.description,
            thumbnail = feed.thumbnail,
            author = feed.author,
            url = feed.url,
            topic = feed.topic,
            type = feed.type,
            publishedDate = feed.publishedDate,
        )
    }

    fun mapToListCacheEntity(feeds: List<Feed>): List<FeedCacheEntity> {
        return feeds.map{
            mapToCacheEntity(it)
        }
    }

    fun mapToListNetworkEntity(feeds: List<FeedCacheEntity>): List<Feed> {
        return feeds.map{
            mapToNetworkEntity(it)
        }
    }

}