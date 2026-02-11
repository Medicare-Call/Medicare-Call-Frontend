package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElderBulkHealthInfoRequestDto(
    @SerialName("healthInfos")
    val healthInfos: List<HealthInfo>,
) {
    @Serializable
    data class HealthInfo(
        @SerialName("elderId")
        val elderId: Long,
        @SerialName("diseaseNames")
        val diseaseNames: List<String>,
        @SerialName("medicationSchedules")
        val medicationSchedules: List<MedicationSchedule>,
        @SerialName("notes")
        val notes: List<String>,
    ) {
        @Serializable
        data class MedicationSchedule(
            @SerialName("medicationName")
            val medicationName: String,
            @SerialName("scheduleTimes")
            val scheduleTimes: List<String>,
        )
    }
}
