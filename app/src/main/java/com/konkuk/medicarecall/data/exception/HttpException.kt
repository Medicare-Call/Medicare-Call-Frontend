package com.konkuk.medicarecall.data.exception

import de.jensklingenberg.ktorfit.Response

class HttpException(response: Response<*>) : RuntimeException(getMessage(response)) {
    private val _code: Int = response.code
    private val _message: String = response.message

    @Transient
    private val response: Response<*>? = response

    fun code(): Int = _code
    fun message(): String = _message
    fun response(): Response<*>? = response

    companion object {
        private fun getMessage(response: Response<*>): String {
            return "HTTP ${response.code} ${response.message}"
        }
    }
}
