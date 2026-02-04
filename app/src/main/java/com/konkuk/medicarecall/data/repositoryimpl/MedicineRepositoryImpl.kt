package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MedicineService
import com.konkuk.medicarecall.data.mapper.toMedicineUiStates
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.MedicineRepository
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatusItem
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MedicineRepositoryImpl(
    private val medicineService: MedicineService,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : MedicineRepository {

    /** 설정 스케줄을 “회색 카드” UI로 변환 */
    override suspend fun getConfiguredMedicineUiList(elderId: Int): List<MedicineUiState> {
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
            val labels = timesByMed[name].orEmpty().let { lst ->
                if (lst.size >= goal) lst.take(goal) else lst + List(goal - lst.size) { "" }
            }
            MedicineUiState(
                medicineName = name,
                todayRequiredCount = goal,
                doseStatusList = labels.map { lab ->
                    DoseStatusItem(time = lab, doseStatus = DoseStatus.NOT_RECORDED) // 회색
                },
            )
        }
    }

    /** 날짜별 기록 호출 + 없으면 스케줄 fallback */
    override suspend fun getMedicineUiStateList(
        elderId: Int,
        date: LocalDate,
    ): List<MedicineUiState> {

        val fallback =
            runCatching { getConfiguredMedicineUiList(elderId) }
                .getOrDefault(emptyList())

        return runCatching {
            medicineService.getDailyMedication(elderId, date.toString())
        }.fold(
            onSuccess = { res ->
                if (!res.isSuccessful) return fallback
                val dto = res.body() ?: return fallback

                dto.toMedicineUiStates(fallback)
            },
            onFailure = {
                fallback
            }
        )
    }
}
