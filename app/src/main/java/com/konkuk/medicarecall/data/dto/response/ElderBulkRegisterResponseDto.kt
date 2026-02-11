package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ElderBulkRegisterResponseDto = List<ElderInfoDto>

@Serializable
data class ElderInfoDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("relationship")
    val relationship: String,
    @SerialName("residenceType")
    val residenceType: String,
    @SerialName("guardianId")
    val guardianId: Int,
    @SerialName("guardianName")
    val guardianName: String,
)
