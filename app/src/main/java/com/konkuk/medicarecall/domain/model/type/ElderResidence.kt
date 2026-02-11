package com.konkuk.medicarecall.domain.model.type

// 혼자계세요, 가족과 함께 살아요
enum class ElderResidence(val displayName: String) {
    ALONE("혼자 계세요"),
    WITH_FAMILY("가족과 함께 살아요"),
    ;

    companion object Companion {
        fun fromString(value: String): ElderResidence {
            return ElderResidence.entries.find { it.displayName == value || it.name == value } ?: ALONE
        }
    }
}
