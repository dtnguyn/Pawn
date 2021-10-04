package com.nguyen.polyglot.db.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.nguyen.polyglot.UserDs
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserDs> {

    override val defaultValue: UserDs
        get() = UserDs.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDs {
        try {
            return UserDs.parseFrom(input)
        } catch(exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserDs, output: OutputStream) {
        return t.writeTo(output)
    }
}

val Context.userDataStore: DataStore<UserDs> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)


