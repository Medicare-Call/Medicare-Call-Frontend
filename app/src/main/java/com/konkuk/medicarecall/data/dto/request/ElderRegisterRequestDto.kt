package com.konkuk.medicarecall.data.dto.request

import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import kotlinx.serialization.Serializable

@Serializable
data class ElderRegisterRequestDto(
    val name: String,
    val birthDate: String,
    val gender: GenderType,
    val phone: String,
    val relationship: RelationshipType,
    val residenceType: ElderResidenceType,
)
