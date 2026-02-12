package com.konkuk.medicarecall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ElderIds(
    val elderIds: Map<Long, String>,
)
