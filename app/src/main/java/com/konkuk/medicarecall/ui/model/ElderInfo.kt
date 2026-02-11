package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

data class ElderInfo(
    val elderId: Int,
    val name: String,
    val birthDate: String,
    val gender: GenderType,
    val phone: String,
    val relationship: Relationship,
    val residenceType: ElderResidence,
)
