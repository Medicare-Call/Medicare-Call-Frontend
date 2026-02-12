package com.konkuk.medicarecall.domain.model.type

import kotlinx.serialization.Serializable

// 자식, 손자, 형제, 친척, 지인
@Serializable
enum class Relationship(val displayName: String) {
    CHILD("자식"),
    GRANDCHILD("손자"),
    SIBLING("형제"),
    RELATIVE("친척"),
    ACQUAINTANCE("지인"),
    ;

    companion object Companion {
        fun fromString(value: String): Relationship {
            return Relationship.entries.find { it.displayName == value || it.name == value } ?: CHILD
        }
    }
}
