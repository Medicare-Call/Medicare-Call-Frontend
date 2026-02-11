package com.konkuk.medicarecall.data.dto.response

import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EldersInfoResponseDto(
    val elderId: Long,
    val name: String,
    val birthDate: String,
    val gender: GenderType,
    val phone: String,
    val relationship: Relationship,
    val residenceType: ElderResidence,
)

@Serializable
data class ElderResponseDto(
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("elderId")
    val elderId: Long,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("relationship")
    val relationship: String,
    @SerialName("residenceType")
    val residenceType: String,
)
