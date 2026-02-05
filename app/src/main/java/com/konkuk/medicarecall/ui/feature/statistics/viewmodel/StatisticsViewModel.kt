package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.exception.HttpException
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@KoinViewModel
class StatisticsViewModel(
    private val repository: StatisticsRepository,
    private val eldersIdRepository: ElderIdRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    // [수정 1] earliestDate의 초기값을 아주 먼 과거로 설정하여 초기 오류를 방지합니다.
    private var earliestDate: LocalDate = LocalDate.MIN
    private var lastFetchTime: Long = 0

    init {
        viewModelScope.launch {
            _uiState
                .map { it.selectedElderId to it.currentWeek }
                .distinctUntilChanged()
                .collect { (id, week) ->
                    if (id != null) {
                        // [수정 2] 주차가 변경될 때마다 isLatestWeek와 isEarliestWeek를 다시 계산합니다.
                        // 이렇게 하면 API 호출 성공/실패와 관계없이 UI 상태가 정확해집니다.
                        val weekStart = week.first
                        val isLatest = weekStart == weekStartOf(LocalDate.now())
                        val isEarliest = earliestDate != LocalDate.MIN && weekStart == weekStartOf(earliestDate)

                        _uiState.update {
                            it.copy(isLatestWeek = isLatest, isEarliestWeek = isEarliest)
                        }

                        getWeeklyStatistics(elderId = id, startDate = weekStart)
                    }
                }
            _uiState.update { it.copy(eldersMap = eldersIdRepository.getElderIds()) }
        }
    }

    fun onMedsChanged() {
        refresh()
    }

    fun refresh() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFetchTime < 60000) {
            return
        }

        val id = _uiState.value.selectedElderId ?: return
        val start = _uiState.value.currentWeek.first
        getWeeklyStatistics(
            elderId = id,
            startDate = start,
            ignoreLoadingGate = true,
        )
    }

    fun setSelectedElderId(id: Int) {
        if (_uiState.value.selectedElderId != id) {
            // [수정 3] 새로운 사용자를 선택하면 earliestDate를 초기화합니다.
            // 이렇게 해야 이전 사용자의 기록이 다음 사용자에게 영향을 주지 않습니다.
            earliestDate = LocalDate.MIN
            _uiState.update { it.copy(selectedElderId = id) }
        }
    }

    /* ---------------- Week 이동 ---------------- */

    fun showPreviousWeek() {
        // [수정 4] isEarliestWeek 상태를 직접 신뢰하여 UI 이동을 막습니다.
        // 이 상태는 collect 블록에서 안정적으로 관리됩니다.
        if (_uiState.value.isEarliestWeek) return
        _uiState.update { it.copy(currentWeek = getWeekRange(it.currentWeek.first.minusWeeks(1))) }
    }

    fun showNextWeek() {
        if (_uiState.value.isLatestWeek) return
        _uiState.update { it.copy(currentWeek = getWeekRange(it.currentWeek.first.plusWeeks(1))) }
    }

    fun jumpToTodayWeek() = jumpToWeekOf(LocalDate.now())
    fun jumpToWeekOf(date: LocalDate) {
        _uiState.update { it.copy(currentWeek = getWeekRange(date)) }
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getStatistics(elderId, startDate.toString())
                .onSuccess { data ->
                    lastFetchTime = System.currentTimeMillis()
                    if (earliestDate == LocalDate.MIN) {
                        earliestDate = LocalDate.parse(data.subscriptionStartDate)
                        val isEarliest = _uiState.value.currentWeek.first == weekStartOf(earliestDate)
                        _uiState.update { it.copy(isEarliestWeek = isEarliest) }
                    }

                    val summary = WeeklySummaryUiState.from(data, data.medicationStats?.keys?.toList() ?: emptyList())

                    _uiState.update {
                        it.copy(isLoading = false, summary = summary, error = null)
                    }
                }.onFailure { e ->
                    val summaryState = if (e is HttpException && e.code() == 404) {
                        WeeklySummaryUiState.Companion.EMPTY
                    } else null // 그 외의 오류는 error 메시지로 표시

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            summary = summaryState,
                            error = if (summaryState == null) "데이터 로딩 실패: ${e.message}" else null,
                        )
                    }
                }
        }
    }
}
