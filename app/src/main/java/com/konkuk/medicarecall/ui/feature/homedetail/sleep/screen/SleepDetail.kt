package com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.feature.calendar.DateSelector
import com.konkuk.medicarecall.ui.feature.calendar.WeeklyCalendar
import com.konkuk.medicarecall.ui.feature.calendar.viewmodel.CalendarUiState
import com.konkuk.medicarecall.ui.feature.calendar.viewmodel.CalendarViewModel
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.component.SleepDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepUiState
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SleepDetail(
    onBack: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    sleepViewModel: SleepViewModel = hiltViewModel(),
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        calendarViewModel.resetToToday()
    }

    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val sleep by sleepViewModel.sleep.collectAsState()

    // 네임드롭에서 선택된 어르신
    val elderId by homeViewModel.selectedElderId.collectAsState()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        elderId?.let { id ->
            sleepViewModel.loadSleepDataForDate(id, selectedDate)
        }
    }

    SleepDetailLayout(
        modifier = Modifier,
        onBack = onBack,
        selectedDate = selectedDate,
        sleep = sleep,
        weekDates = calendarViewModel.getCurrentWeekDates(),
        onDateSelected = { calendarViewModel.selectDate(it) },
        onMonthClick = { /* 모달 열기 */ },
    )
}

@Composable
fun SleepDetailLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    sleep: SleepUiState,
    weekDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        TopAppBar(
            title = "수면",
            onBack = onBack,
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .background(MediCareCallTheme.colors.bg)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            DateSelector(
                selectedDate = selectedDate,
                onMonthClick = onMonthClick,
                onDateSelected = onDateSelected,
            )
            Spacer(Modifier.height(12.dp))
            WeeklyCalendar(
                calendarUiState = CalendarUiState(
                    currentYear = selectedDate.year,
                    currentMonth = selectedDate.monthValue,
                    weekDates = weekDates,
                    selectedDate = selectedDate,
                ),
                onDateSelected = onDateSelected,
            )
            Spacer(modifier = Modifier.height(32.dp))
            SleepDetailCard(
                sleep,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSleepDetail() {
    MediCareCallTheme {
        SleepDetailLayout(
            onBack = {},
            selectedDate = LocalDate.now(),
            sleep = SleepUiState.Companion.EMPTY,
            weekDates = (0..6).map { LocalDate.now().plusDays(it.toLong()) },
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
