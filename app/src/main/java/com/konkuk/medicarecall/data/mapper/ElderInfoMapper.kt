package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription

object ElderInfoMapper {

    // ResponseDto → Domain Model
    fun toDomain(dto: EldersInfoResponseDto): ElderInfo {
        return ElderInfo(
            elderId = dto.elderId,
            name = dto.name,
            birthDate = dto.birthDate,
            gender = dto.gender,
            phone = dto.phone,
            relationship = dto.relationship,
            residenceType = dto.residenceType,
        )
    }

    // Domain Model → RequestDto
    fun toRequestDto(model: ElderInfo): ElderRegisterRequestDto {
        return ElderRegisterRequestDto(
            name = model.name,
            birthDate = model.birthDate,
            gender = model.gender,
            phone = model.phone,
            relationship = model.relationship,
            residenceType = model.residenceType,
        )
    }

    // ElderData → RequestDto
    fun elderDataToRequestDto(model: ElderInfo): ElderRegisterRequestDto {
        return ElderRegisterRequestDto(
            name = model.name,
            birthDate = model.birthDate.formatAsDate(),
            gender = model.gender,
            phone = model.phone,
            relationship = model.relationship,
            residenceType = model.residenceType,
        )
    }

    // Subscription ResponseDto → Domain Model
    fun subscriptionToDomain(dto: EldersSubscriptionResponseDto): ElderSubscription {
        return ElderSubscription(
            elderId = dto.elderId,
            name = dto.name,
            plan = dto.plan,
            price = dto.price,
            nextBillingDate = dto.nextBillingDate,
            startDate = dto.startDate,
        )
    }
}
