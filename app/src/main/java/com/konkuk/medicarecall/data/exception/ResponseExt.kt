package com.konkuk.medicarecall.data.exception

import de.jensklingenberg.ktorfit.Response

fun <T> Response<T>.getOrThrow(): T {
    if (isSuccessful) {
        return body() ?: error("Response body is null")
    } else {
        throw HttpException(this)
    }
}
