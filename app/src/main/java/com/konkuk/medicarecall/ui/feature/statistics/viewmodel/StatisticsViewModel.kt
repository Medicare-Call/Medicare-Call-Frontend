package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MedicationStatDto
import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val summary: WeeklySummaryUiState? = null,
    val error: String? = null,
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: StatisticsRepository,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    private val _selectedElderId = MutableStateFlow<Int?>(null)

    private val _currentWeek = MutableStateFlow(getWeekRange(LocalDate.now()))
    val currentWeek: StateFlow<Pair<LocalDate, LocalDate>> = _currentWeek

    private val _isLatestWeek = MutableStateFlow(true)
    val isLatestWeek: StateFlow<Boolean> = _isLatestWeek

    private val _isEarliestWeek = MutableStateFlow(false)
    val isEarliestWeek: StateFlow<Boolean> = _isEarliestWeek

    // [수정 1] earliestDate의 초기값을 아주 먼 과거로 설정하여 초기 오류를 방지합니다.
    private var earliestDate: LocalDate = LocalDate.MIN
    private var lastFetchTime: Long = 0

    init {
        viewModelScope.launch {
            _selectedElderId
                .combine(_currentWeek) { id, week -> id to week }
                .distinctUntilChanged()
                .collect { (id, week) ->
                    if (id != null) {
                        // [수정 2] 주차가 변경될 때마다 isLatestWeek와 isEarliestWeek를 다시 계산합니다.
                        // 이렇게 하면 API 호출 성공/실패와 관계없이 UI 상태가 정확해집니다.
                        val weekStart = week.first
                        _isLatestWeek.value = weekStart == weekStartOf(LocalDate.now())
                        // earliestDate가 아직 초기값(LocalDate.MIN)이 아닐 때만 계산합니다.
                        _isEarliestWeek.value = earliestDate != LocalDate.MIN && weekStart == weekStartOf(earliestDate)

                        getWeeklyStatistics(elderId = id, startDate = weekStart)
                    }
                }
        }
    }

    fun refresh() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFetchTime < 60000) {
            return
        }

        val id = _selectedElderId.value ?: return
        val start = _currentWeek.value.first
        getWeeklyStatistics(
            elderId = id,
            startDate = start,
            ignoreLoadingGate = true,
        )
    }

    fun setSelectedElderId(id: Int) {
        if (_selectedElderId.value != id) {
            // [수정 3] 새로운 사용자를 선택하면 earliestDate를 초기화합니다.
            // 이렇게 해야 이전 사용자의 기록이 다음 사용자에게 영향을 주지 않습니다.
            earliestDate = LocalDate.MIN
            _selectedElderId.value = id
        }
    }

    /* ---------------- Week 이동 ---------------- */

    fun showPreviousWeek() {
        // [수정 4] isEarliestWeek 상태를 직접 신뢰하여 UI 이동을 막습니다.
        // 이 상태는 collect 블록에서 안정적으로 관리됩니다.
        if (_isEarliestWeek.value) return
        _currentWeek.value = getWeekRange(_currentWeek.value.first.minusWeeks(1))
    }

    fun showNextWeek() {
        if (_isLatestWeek.value) return
        _currentWeek.value = getWeekRange(_currentWeek.value.first.plusWeeks(1))
    }

    fun jumpToTodayWeek() = jumpToWeekOf(LocalDate.now())
    fun jumpToWeekOf(date: LocalDate) {
        _currentWeek.value = getWeekRange(date)
    }

    // [삭제 1] updateWeekState 함수는 이제 init 블록의 로직으로 대체되었으므로 삭제합니다.
    // private fun updateWeekState(weekStart: LocalDate) { ... }
    /* ---------------- Week 계산 ---------------- */
    private fun getWeekRange(date: LocalDate): Pair<LocalDate, LocalDate> {
        val start = weekStartOf(date)
        return start to start.plusDays(6)
    }

    private fun weekStartOf(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    /* ---------------- 데이터 로딩 ---------------- */

    private fun getWeeklyStatistics(
        elderId: Int,
        startDate: LocalDate,
        ignoreLoadingGate: Boolean = false,
    ) {
        if (_uiState.value.isLoading && !ignoreLoadingGate) return

        // [삭제 2] 이 검사는 더 이상 필요 없으므로 삭제합니다.
        // if (isEarliestWeek.value) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                val formatted = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

                val correctOrder: List<String> =
                    eldersHealthInfoRepository.getEldersHealthInfo()
                        .getOrNull()
                        ?.firstOrNull { it.elderId == elderId }
                        ?.medications
                        ?.flatMap { it.value }
                        ?.distinct()
                        ?: emptyList()

                repository.getStatistics(elderId, formatted) to correctOrder
            }.onSuccess { (dto, order) ->
                lastFetchTime = System.currentTimeMillis()
                // [수정 5] API 호출 성공 시 earliestDate를 처음 한 번만 설정합니다.
                if (earliestDate == LocalDate.MIN) {
                    earliestDate = LocalDate.parse(dto.subscriptionStartDate)
                    // earliestDate가 갱신되었으므로, 현재 주차가 가장 이른 주차인지 다시 확인합니다.
                    _isEarliestWeek.value = _currentWeek.value.first == weekStartOf(earliestDate)
                }

                Log.d("STATISTICS_DEBUG", "onSuccess: DTO 수신 완료\n$dto")
                val summary = dto.toWeeklySummaryUiState(order)
                Log.d("STATISTICS_DEBUG", "onSuccess: UI State로 변환 완료\n$summary")

                _uiState.value = StatisticsUiState(
                    isLoading = false,
                    summary = summary,
                    error = null,
                )
            }.onFailure { e ->
                Log.e("STATISTICS_DEBUG", "onFailure: 데이터 로딩 실패", e)

                val summaryState = if (e is HttpException && e.code() == 404) {
                    // 데이터가 없는 경우(404)는 오류 메시지 없이 빈 상태를 표시합니다.
                    WeeklySummaryUiState.Companion.EMPTY
                } else {
                    null // 그 외의 오류는 error 메시지로 표시
                }

                _uiState.value = StatisticsUiState(
                    isLoading = false,
                    summary = summaryState,
                    error = if (summaryState == null) "데이터 로딩 실패: ${e.message}" else null,
                )
            }
        }
    }

    // toWeeklySummaryUiState 함수는 변경 없음 (생략)
    private fun StatisticsResponseDto.toWeeklySummaryUiState(correctOrder: List<String>): WeeklySummaryUiState {
        // ... (이전 코드와 동일)
        val indexOf: (String) -> Int = { key ->
            val idx = correctOrder.indexOf(key)
            if (idx == -1) Int.MAX_VALUE else idx
        }

        val orderedMedicines = medicationStats.entries
            .sortedWith(
                compareBy<Map.Entry<String, MedicationStatDto>>(
                    { indexOf(it.key) },
                    { it.key },
                ),
            )
            .map { (name, stats) ->
                WeeklyMedicineUiState(
                    medicineName = name,
                    takenCount = stats.takenCount,
                    totalCount = stats.totalCount,
                )
            }

        val sleepH = averageSleep.hours
        val sleepM = averageSleep.minutes

        return WeeklySummaryUiState(
            elderName = elderName,
            weeklyMealRate = summaryStats.mealRate,
            weeklyMedicineRate = summaryStats.medicationRate,
            weeklyHealthIssueCount = summaryStats.healthSignals,
            weeklyUnansweredCount = summaryStats.missedCalls,
            weeklyMeals = listOf(
                WeeklyMealUiState("아침", mealStats.breakfast, 7),
                WeeklyMealUiState("점심", mealStats.lunch, 7),
                WeeklyMealUiState("저녁", mealStats.dinner, 7),
            ),
            weeklyMedicines = orderedMedicines,
            weeklyHealthNote = healthSummary,
            weeklySleepHours = sleepH,
            weeklySleepMinutes = sleepM,
            weeklyMental = WeeklyMentalUiState(
                good = psychSummary.good,
                normal = psychSummary.normal,
                bad = psychSummary.bad,
            ),
            weeklyGlucose = WeeklyGlucoseUiState(
                beforeMealNormal = bloodSugar.beforeMeal.normal,
                beforeMealHigh = bloodSugar.beforeMeal.high,
                beforeMealLow = bloodSugar.beforeMeal.low,
                afterMealNormal = bloodSugar.afterMeal.normal,
                afterMealHigh = bloodSugar.afterMeal.high,
                afterMealLow = bloodSugar.afterMeal.low,
            ),
        )
    }
}
