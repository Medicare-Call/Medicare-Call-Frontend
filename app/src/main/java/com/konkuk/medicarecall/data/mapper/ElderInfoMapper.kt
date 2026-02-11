package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

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
    fun elderDataToRequestDto(model: ElderData): ElderRegisterRequestDto {
        return ElderRegisterRequestDto(
            name = model.name,
            birthDate = model.birthDate.formatAsDate(),
            gender = if (model.gender) GenderType.MALE else GenderType.FEMALE,
            phone = model.phoneNumber,
            relationship = Relationship.entries.find { it.displayName == model.relationship }
                ?: Relationship.ACQUAINTANCE,
            residenceType = ElderResidence.entries.find { it.displayName == model.livingType }
                ?: ElderResidence.WITH_FAMILY,
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
