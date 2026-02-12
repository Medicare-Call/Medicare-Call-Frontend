package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.exception.HttpException
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.domain.util.MIN
import com.konkuk.medicarecall.domain.util.getWeekRange
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.domain.util.weekStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.android.annotation.KoinViewModel

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
                        val isLatest = weekStart == LocalDate.now().weekStart()
                        val isEarliest = earliestDate != LocalDate.MIN && weekStart == earliestDate.weekStart()

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

        val id = _uiState.value.selectedElderId
        if (id == -1L) return
        val start = _uiState.value.currentWeek.first
        getWeeklyStatistics(
            elderId = id,
            startDate = start,
            ignoreLoadingGate = true,
        )
    }

    fun setSelectedElderId(id: Long) {
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
        _uiState.update { it.copy(currentWeek = it.currentWeek.first.minus(1, DateTimeUnit.WEEK).getWeekRange()) }
    }

    fun showNextWeek() {
        if (_uiState.value.isLatestWeek) return
        _uiState.update { it.copy(currentWeek = it.currentWeek.first.plus(1, DateTimeUnit.WEEK).getWeekRange()) }
    }

    fun jumpToTodayWeek() = jumpToWeekOf(LocalDate.now())
    fun jumpToWeekOf(date: LocalDate) {
        _uiState.update { it.copy(currentWeek = date.getWeekRange()) }
    }

    private fun getWeeklyStatistics(
        elderId: Long,
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
                        earliestDate = LocalDate.parse(data.subscriptionStartDate ?: "1970-01-01")
                        val isEarliest = _uiState.value.currentWeek.first == earliestDate.weekStart()
                        _uiState.update { it.copy(isEarliestWeek = isEarliest) }
                    }

                    val summary = WeeklySummaryUiState.from(data, data.medicationStats?.keys?.toList() ?: emptyList())

                    _uiState.update {
                        it.copy(isLoading = false, summary = summary, error = null)
                    }
                }.onFailure { e ->
                    val summaryState = if (e is HttpException && e.code() == 404) {
                        WeeklySummaryUiState()
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
