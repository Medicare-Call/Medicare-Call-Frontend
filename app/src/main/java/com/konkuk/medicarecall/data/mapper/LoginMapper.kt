package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.ElderBulkRegisterResponseDto
import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.domain.model.MemberStatus
import com.konkuk.medicarecall.domain.model.Verification
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

fun VerificationResponseDto.toModel(): Verification =
    when (MemberStatus.fromString(this.memberStatus)) {
        MemberStatus.NEW_MEMBER -> Verification.NewMember(
            verified = this.verified,
            message = this.message,
            newUserToken = this.token ?: "",
            memberStatus = MemberStatus.NEW_MEMBER,
        )

        MemberStatus.EXISTING_MEMBER -> Verification.ExistingMember(
            verified = this.verified,
            message = this.message,
            accessToken = this.accessToken ?: "",
            refreshToken = this.refreshToken ?: "",
            memberStatus = MemberStatus.EXISTING_MEMBER,
        )
    }

fun ElderBulkRegisterResponseDto.toModel(): List<Elder> = this.map {
    Elder(
        info = ElderInfo(
            elderId = it.id.toLong(),
            name = it.name,
            birthDate = it.birthDate,
            gender = GenderType.fromString(it.gender),
            phone = it.phone,
            relationship = Relationship.fromString(it.relationship),
            residenceType = ElderResidence.fromString(it.residenceType),
        ),
    )
}
