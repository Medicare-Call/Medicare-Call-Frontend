package com.konkuk.medicarecall.domain.model.type

import kotlinx.serialization.Serializable

@Serializable
enum class GenderType(val displayName: String) {
    MALE("남자"),
    FEMALE("여자"),
    ;

    companion object {
        fun fromString(value: String): GenderType {
            return entries.find { it.displayName == value || it.name == value } ?: MALE
        }
    }
}
