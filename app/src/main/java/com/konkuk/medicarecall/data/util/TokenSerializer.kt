package com.konkuk.medicarecall.data.util

import androidx.datastore.core.Serializer
import com.konkuk.medicarecall.data.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

/**
 * https://www.youtube.com/watch?v=XMaQNN9YpKk
 */
object TokenSerializer : Serializer<Token> {
    override val defaultValue: Token
        get() = Token(null, null)

    override suspend fun readFrom(input: InputStream): Token {
        return try {
            val encryptedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }

            // 빈 파일인 경우 기본값 반환
            if (encryptedBytes.isEmpty()) {
                return defaultValue
            }

            val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
            val decryptedBytes = TokenEncryptor.decrypt(encryptedBytesDecoded)
            val decodedJsonString = decryptedBytes.decodeToString()
            Json.decodeFromString(decodedJsonString)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: Token, output: OutputStream) {
        val json = Json.Default.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = TokenEncryptor.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}
