package com.nguyen.polyglot.db.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.nguyen.polyglot.TokenDs
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<TokenDs> {

    override val defaultValue: TokenDs
        get() = TokenDs.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenDs {
        try {
            return TokenDs.parseFrom(input)
        } catch(exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: TokenDs, output: OutputStream) {
        return t.writeTo(output)
    }
}

val Context.tokenDataStore: DataStore<TokenDs> by dataStore(
    fileName = "token.pb",
    serializer = TokenSerializer
)


