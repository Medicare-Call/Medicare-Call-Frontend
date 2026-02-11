package com.konkuk.medicarecall.data.dto.request

import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship
import kotlinx.serialization.Serializable

@Serializable
data class ElderRegisterRequestDto(
    val name: String,
    val birthDate: String,
    val gender: GenderType,
    val phone: String,
    val relationship: Relationship,
    val residenceType: ElderResidence,
)
