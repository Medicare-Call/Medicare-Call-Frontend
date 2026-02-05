package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import com.konkuk.medicarecall.domain.model.MemberStatus
import com.konkuk.medicarecall.domain.model.Verification

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
