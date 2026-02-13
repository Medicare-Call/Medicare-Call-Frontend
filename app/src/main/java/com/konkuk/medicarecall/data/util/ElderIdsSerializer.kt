package com.konkuk.medicarecall.data.util

import androidx.datastore.core.Serializer
import com.konkuk.medicarecall.data.model.ElderIds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

/**
 * https://www.youtube.com/watch?v=XMaQNN9YpKk
 */
object ElderIdsSerializer : Serializer<ElderIds> {
    override val defaultValue: ElderIds
        get() = ElderIds(emptyMap())

    override suspend fun readFrom(input: InputStream): ElderIds {
        return try {
            val encodedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }

            // 빈 파일인 경우 기본값 반환
            if (encodedBytes.isEmpty()) {
                return defaultValue
            }

            val decodedBytesBase64 = Base64.getDecoder().decode(encodedBytes)
            val decodedJsonString = decodedBytesBase64.decodeToString()
            Json.decodeFromString(decodedJsonString)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ElderIds, output: OutputStream) {
        val json = Json.Default.encodeToString(t)
        val bytes = json.toByteArray()
        val encodedBytesBase64 = Base64.getEncoder().encode(bytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encodedBytesBase64)
            }
        }
    }
}
