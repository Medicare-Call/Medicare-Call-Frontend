package com.konkuk.medicarecall.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import kotlinx.serialization.json.Json

// NavType definitions for complex DTOs
val EldersInfoResponseDtoType = object : NavType<EldersInfoResponseDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): EldersInfoResponseDto? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): EldersInfoResponseDto {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: EldersInfoResponseDto): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: EldersInfoResponseDto) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

val EldersHealthResponseDtoType = object : NavType<EldersHealthResponseDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): EldersHealthResponseDto? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): EldersHealthResponseDto {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: EldersHealthResponseDto): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: EldersHealthResponseDto) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

val EldersSubscriptionResponseDtoType = object : NavType<EldersSubscriptionResponseDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): EldersSubscriptionResponseDto? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): EldersSubscriptionResponseDto {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: EldersSubscriptionResponseDto): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: EldersSubscriptionResponseDto) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

val NoticesResponseDtoType = object : NavType<NoticesResponseDto>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): NoticesResponseDto? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): NoticesResponseDto {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: NoticesResponseDto): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: NoticesResponseDto) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
