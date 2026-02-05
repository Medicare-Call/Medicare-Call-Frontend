package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MedicineService
import com.konkuk.medicarecall.data.mapper.toMedicines
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.MedicineRepository
import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MedicineRepositoryImpl(
    private val medicineService: MedicineService,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : MedicineRepository {
    override suspend fun getConfiguredMedicines(elderId: Int): List<Medicine> {
        val schedule = eldersHealthInfoRepository.getEldersHealthInfo()
            .getOrNull()
            ?.firstOrNull { it.elderId == elderId }
            ?.medications
            ?: emptyMap()

        // 약 이름별 1일 횟수 + 시간 라벨(아침/점심/저녁)
        val countByMed = linkedMapOf<String, Int>()
        val timesByMed = linkedMapOf<String, MutableList<String>>()
        schedule.forEach { (timeEnum, meds) ->
            val label = when (timeEnum.name) {
                "MORNING" -> "아침"
                "LUNCH" -> "점심"
                "DINNER" -> "저녁"
                else -> ""
            }
            meds.forEach { raw ->
                val name = raw.trim()
                if (name.isNotEmpty()) {
                    countByMed[name] = (countByMed[name] ?: 0) + 1
                    timesByMed.getOrPut(name) { mutableListOf() }.add(label)
                }
            }
        }

        if (countByMed.isEmpty()) return emptyList()

        return countByMed.map { (name, goal) ->
            val times = timesByMed[name].orEmpty()
            val timeEnums = times.map { label ->
                when (label) {
                    "아침" -> "MORNING"
                    "점심" -> "LUNCH"
                    "저녁" -> "DINNER"
                    else -> ""
                }
            }

            Medicine(
                medicineName = name,
                todayRequiredCount = goal,
                doseStatusList = timeEnums.map { timeEnum ->
                    DoseStatusItem(time = timeEnum, doseStatus = DoseStatus.NOT_RECORDED)
                },
            )
        }
    }

    /** 날짜별 기록 호출 + 없으면 스케줄 fallback */
    override suspend fun getMedicines(
        elderId: Int,
        date: LocalDate,
    ): List<Medicine> {
        val fallback =
            runCatching { getConfiguredMedicines(elderId) }
                .getOrDefault(emptyList())

        return runCatching {
            medicineService.getDailyMedication(elderId, date.toString())
        }.fold(
            onSuccess = { res ->
                if (!res.isSuccessful) return fallback
                val dto = res.body() ?: return fallback

                val medicines = dto.toMedicines()
                if (medicines.isEmpty()) fallback else medicines
            },
            onFailure = {
                fallback
            },
        )
    }
}
