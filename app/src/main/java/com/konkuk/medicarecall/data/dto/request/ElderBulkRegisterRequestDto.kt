package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElderBulkRegisterRequestDto(
    @SerialName("elders")
    val elders: List<ElderInfo>,
) {
    @Serializable
    data class ElderInfo(
        @SerialName("name")
        val name: String,
        @SerialName("birthDate")
        val birthDate: String,
        @SerialName("gender")
        val gender: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("relationship")
        val relationship: String,
        @SerialName("residenceType")
        val residenceType: String,
    )
}
