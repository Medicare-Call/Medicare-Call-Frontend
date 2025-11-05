package com.konkuk.medicarecall.data.util

import androidx.datastore.core.Serializer
import com.konkuk.medicarecall.data.model.FcmToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object FcmTokenSerializer : Serializer<FcmToken> {
    override val defaultValue: FcmToken
        get() = FcmToken(null)

    override suspend fun readFrom(input: InputStream): FcmToken {
        val raw = withContext(Dispatchers.IO) {
            input.readBytes()
        }
        if (raw.isEmpty()) {
            return FcmToken(null)
        } // 파일 비어있거나 한 번도 저장 안했을 때 빈 배열 예외 처리

        val encryptedBytesDecoded = Base64.getDecoder().decode(raw)
        val decryptedBytes = FcmTokenEncryptor.decrypt(encryptedBytesDecoded)
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(t: FcmToken, output: OutputStream) {
        val json = Json.Default.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = FcmTokenEncryptor.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)

        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}
