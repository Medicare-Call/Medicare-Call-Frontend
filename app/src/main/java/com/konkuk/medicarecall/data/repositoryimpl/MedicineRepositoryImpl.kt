package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MedicineService
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.MedicineRepository
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatusItem
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import java.time.LocalDate
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
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
        val grayTemplate =
            runCatching { getConfiguredMedicineUiList(elderId) }.getOrDefault(emptyList())

        return runCatching { medicineService.getDailyMedication(elderId, date.toString()) }
            .fold(
                onSuccess = { res ->
                    if (res.isSuccessful) {
                        val dto = res.body()
                        if (dto == null || dto.medications.isEmpty()) {
                            // 성공이지만 자료 없음 → 스케줄로 대체
                            return@fold if (grayTemplate.isNotEmpty()) grayTemplate else emptyList()
                        }

                        // --- 순서 정렬 로직 시작 ---

                        val correctOrder = grayTemplate.map { it.medicineName }

                        val sortedMedications = dto.medications.sortedBy { medDto ->
                            correctOrder.indexOf(medDto.type)
                                .let { if (it == -1) Int.MAX_VALUE else it }
                        }

                        val order = listOf("MORNING", "LUNCH", "DINNER")
                        val kor = mapOf("MORNING" to "아침", "LUNCH" to "점심", "DINNER" to "저녁")

                        return@fold sortedMedications.map { m ->
                            val mapped = order.mapNotNull { slot ->
                                m.times.find { it.time == slot }?.let { t ->
                                    DoseStatusItem(
                                        time = kor[slot] ?: slot,
                                        doseStatus = when (t.taken) {
                                            true -> DoseStatus.TAKEN
                                            false -> DoseStatus.SKIPPED
                                            null -> DoseStatus.NOT_RECORDED
                                        },
                                    )
                                }
                            }
                            val padded = if (mapped.size < m.goalCount) {
                                mapped + List(m.goalCount - mapped.size) {
                                    DoseStatusItem(time = "", doseStatus = DoseStatus.NOT_RECORDED)
                                }
                            } else mapped.take(m.goalCount)

                            MedicineUiState(
                                medicineName = m.type,
                                todayRequiredCount = m.goalCount,
                                doseStatusList = padded,
                            )
                        }
                    } else {
                        grayTemplate.ifEmpty { emptyList() }
                    }
                },
                onFailure = {
                    grayTemplate.ifEmpty { emptyList() }
                },
            )
    }
}
