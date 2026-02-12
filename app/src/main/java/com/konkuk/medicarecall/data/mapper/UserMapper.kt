package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.PushNotificationDto
import com.konkuk.medicarecall.domain.model.UserInfo
import com.konkuk.medicarecall.domain.model.PushNotification

object UserMapper {

    // ResponseDto → Domain Model
    fun toDomain(dto: MyInfoResponseDto): UserInfo {
        return UserInfo(
            name = dto.name,
            birthDate = dto.birthDate,
            gender = dto.gender,
            phoneNumber = dto.phone,
            pushNotification = pushNotificationToDomain(dto.pushNotification),
        )
    }

    // Domain Model → RequestDto (MyInfoResponseDto is used as both request and response)
    fun toRequestDto(model: UserInfo): MyInfoResponseDto {
        return MyInfoResponseDto(
            name = model.name,
            birthDate = model.birthDate,
            gender = model.gender,
            phone = model.phoneNumber,
            pushNotification = pushNotificationToDto(model.pushNotification),
        )
    }

    // PushNotificationDto → Domain Model
    private fun pushNotificationToDomain(dto: PushNotificationDto): PushNotification {
        return PushNotification(
            all = dto.all,
            carecallCompleted = dto.carecallCompleted,
            healthAlert = dto.healthAlert,
            carecallMissed = dto.carecallMissed,
        )
    }

    // Domain Model → PushNotificationDto
    private fun pushNotificationToDto(model: PushNotification): PushNotificationDto {
        return PushNotificationDto(
            all = model.all,
            carecallCompleted = model.carecallCompleted,
            healthAlert = model.healthAlert,
            carecallMissed = model.carecallMissed,
        )
    }
}
