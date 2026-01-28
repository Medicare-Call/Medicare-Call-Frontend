package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.request.ElderHealthRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.MedicationSchedule
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.type.MedicationTimeType
import org.koin.core.annotation.Single

@Single
class EldersHealthInfoRepositoryImpl(
    private val elderInfoService: EldersInfoService,
    private val elderRegisterService: ElderRegisterService,
) : EldersHealthInfoRepository {

    private var cachedHealthInfo: List<EldersHealthResponseDto>? = null

    override fun refresh() {
        Log.d("Cache", "EldersHealthInfoRepository cache invalidated")
        cachedHealthInfo = null
    }

    override suspend fun getEldersHealthInfo(): Result<List<EldersHealthResponseDto>> {
        cachedHealthInfo?.let {
            Log.d("Cache", "Returning cached health info")
            return Result.success(it)
        }

        return runCatching {
            Log.d("Cache", "Fetching new health info from server")
            val body = elderInfoService.getElderHealthInfo().handleResponse()
            cachedHealthInfo = body // 캐시에 저장
            body
        }
    }

    override suspend fun updateHealthInfo(
        elderInfo: EldersHealthResponseDto,
    ): Result<Unit> = runCatching {
        val medicationSchedule = elderInfo.medications.toMedicationSchedules()
        val elder = ElderHealthRegisterRequestDto(
            diseaseNames = elderInfo.diseases,
            medicationSchedules = medicationSchedule,
            notes = elderInfo.notes,
        )
        elderRegisterService.postElderHealthInfo(elderInfo.elderId, elder).handleNullableResponse()
        refresh()
    }

    fun Map<MedicationTimeType, List<String>>.toMedicationSchedules(): List<MedicationSchedule> {
        val timesByMed = linkedMapOf<String, MutableSet<MedicationTimeType>>()
        for ((time, meds) in this) {
            for (med in meds) {
                timesByMed.getOrPut(med.trim()) { linkedSetOf() }.add(time)
            }
        }
        return timesByMed.map { (medName, times) ->
            MedicationSchedule(
                medicationName = medName,
                scheduleTimes = times.sortedBy { it.ordinal },
            )
        }
    }
}
