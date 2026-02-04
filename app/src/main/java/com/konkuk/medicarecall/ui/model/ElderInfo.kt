package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType

data class ElderInfo(
    val elderId: Int,
    val name: String,
    val birthDate: String,
    val gender: GenderType,
    val phone: String,
    val relationship: RelationshipType,
    val residenceType: ElderResidenceType,
)
