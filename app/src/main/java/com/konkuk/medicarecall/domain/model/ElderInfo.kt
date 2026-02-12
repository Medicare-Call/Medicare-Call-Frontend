package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

data class ElderInfo(
    val elderId: Long = 0L,
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phone: String = "",
    val relationship: Relationship = Relationship.CHILD,
    val residenceType: ElderResidence = ElderResidence.ALONE,
)
