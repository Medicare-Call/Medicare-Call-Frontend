package com.konkuk.medicarecall.data.util

import com.konkuk.medicarecall.data.exception.HttpException
import de.jensklingenberg.ktorfit.Response

fun <T> Response<T>.handleResponse(): T {
    if (isSuccessful) {
        return body() ?: error("응답값이 null 입니다.")
    } else {
        throw HttpException(this)
    }
}

// Nullable 응답용
fun <T> Response<T>.handleNullableResponse(): T? {
    if (isSuccessful) {
        return body()
    } else {
        throw HttpException(this)
    }
}
