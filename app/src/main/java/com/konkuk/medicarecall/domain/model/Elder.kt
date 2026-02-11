package com.konkuk.medicarecall.domain.model

data class Elder(
    val info: ElderInfo,
    val healthInfo: ElderHealthInfo = ElderHealthInfo(
        elderId = info.elderId,
        name = info.name,
    ),
)
