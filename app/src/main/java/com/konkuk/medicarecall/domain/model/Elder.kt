package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.Relationship

data class Elder(
    val id: Long = 0L,
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phoneNumber: String = "",
    val relationship: Relationship = Relationship.CHILD,
    val residenceType: ElderResidence = ElderResidence.ALONE,
    val diseases: List<String> = emptyList(),
    val medication: List<Medication> = emptyList(),
    val notes: List<HealthIssueType> = emptyList(),
)
