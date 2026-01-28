package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import com.konkuk.medicarecall.data.exception.HttpException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val summary: WeeklySummaryUiState? = null,
    val error: String? = null,
    val currentWeek: Pair<LocalDate, LocalDate> = LocalDate.now().let {
        val start = it.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        start to start.plusDays(6)
    },
    val isLatestWeek: Boolean = true,
    val isEarliestWeek: Boolean = false,
    val dropdownOpened: Boolean = false,
)

@KoinViewModel
class StatisticsViewModel(
    private val repository: StatisticsRepository,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    private val _selectedElderId = MutableStateFlow<Int?>(null)
    private var earliestDate: LocalDate = LocalDate.MIN
    private var lastFetchTime: Long = 0

    init {
        viewModelScope.launch {
            _selectedElderId
                .combine(_uiState) { id, state -> id to state.currentWeek }
                .distinctUntilChanged()
                .collect { (id, week) ->
                    if (id != null) {
                        val weekStart = week.first
                        val isLatestWeek = weekStart == weekStartOf(LocalDate.now())
                        val isEarliestWeek = earliestDate != LocalDate.MIN && weekStart == weekStartOf(earliestDate)

                        _uiState.value = _uiState.value.copy(
                            isLatestWeek = isLatestWeek,
                            isEarliestWeek = isEarliestWeek,
                        )

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
        val start = _uiState.value.currentWeek.first
        getWeeklyStatistics(
            elderId = id,
            startDate = start,
            ignoreLoadingGate = true,
        )
    }

    fun setSelectedElderId(id: Int) {
        if (_selectedElderId.value != id) {
            earliestDate = LocalDate.MIN
            _selectedElderId.value = id
        }
    }

    /* ---------------- Week 이동 ---------------- */

    fun showPreviousWeek() {
        if (_uiState.value.isEarliestWeek) return
        val newWeek = getWeekRange(_uiState.value.currentWeek.first.minusWeeks(1))
        _uiState.value = _uiState.value.copy(currentWeek = newWeek)
    }

    fun showNextWeek() {
        if (_uiState.value.isLatestWeek) return
        val newWeek = getWeekRange(_uiState.value.currentWeek.first.plusWeeks(1))
        _uiState.value = _uiState.value.copy(currentWeek = newWeek)
    }

    fun jumpToTodayWeek() = jumpToWeekOf(LocalDate.now())
    fun jumpToWeekOf(date: LocalDate) {
        val newWeek = getWeekRange(date)
        _uiState.value = _uiState.value.copy(currentWeek = newWeek)
    }

    fun toggleDropdown(isOpen: Boolean) {
        _uiState.value = _uiState.value.copy(dropdownOpened = isOpen)
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
                if (earliestDate == LocalDate.MIN) {
                    earliestDate = LocalDate.parse(dto.subscriptionStartDate)
                    // earliestDate가 갱신되었으므로, 현재 주차가 가장 이른 주차인지 다시 확인합니다.
                    val isEarliestWeek = _uiState.value.currentWeek.first == weekStartOf(earliestDate)
                    _uiState.value = _uiState.value.copy(isEarliestWeek = isEarliestWeek)
                }

                Log.d("STATISTICS_DEBUG", "onSuccess: DTO 수신 완료\n$dto")
                val summary = WeeklySummaryUiState.from(dto, order)
                Log.d("STATISTICS_DEBUG", "onSuccess: UI State 변환 완료\n$summary")

                _uiState.value = StatisticsUiState(
                    isLoading = false,
                    summary = summary,
                    error = null,
                )
            }.onFailure { e ->
                Log.d("STATISTICS_DEBUG", "404 EMPTY 적용됨: ${WeeklySummaryUiState.EMPTY.weeklyHealthNote}")

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
}
