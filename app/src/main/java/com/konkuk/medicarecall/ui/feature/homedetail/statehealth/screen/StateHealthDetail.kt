package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.component.StateHealthDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate

@Composable
fun StateHealthDetail(
    onBack: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    healthViewModel: HealthViewModel = hiltViewModel(),
) {
    val isLoading = healthViewModel.isLoading.collectAsState()

    val homeViewModel: HomeViewModel = hiltViewModel()

    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        calendarViewModel.resetToToday()
    }

    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val health by healthViewModel.health.collectAsState()

    // 네임드롭에서 선택된 어르신
    val elderId by homeViewModel.selectedElderId.collectAsState()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        elderId?.let { id ->
            healthViewModel.loadHealthDataForDate(id, selectedDate)
        }
    }

    if (!isLoading.value)
        StateHealthDetailLayout(
            modifier = Modifier,
            onBack = onBack,
            selectedDate = selectedDate,
            health = health,
            weekDates = calendarViewModel.getCurrentWeekDates(),
            onDateSelected = { calendarViewModel.selectDate(it) },
            onMonthClick = { /* 모달 열기 */ },
        )
    else
        Box(
            Modifier
                .fillMaxSize()
                .background(color = MediCareCallTheme.colors.white),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MediCareCallTheme.colors.main,
            )
        }
}

@Composable
fun StateHealthDetailLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    health: HealthUiState,
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
            title = "건강징후",
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
            Spacer(Modifier.height(24.dp))
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
            StateHealthDetailCard(
                health = health,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStateHealthDetail() {
    MediCareCallTheme {
        StateHealthDetailLayout(
            onBack = {},
            selectedDate = LocalDate.now(),
            health = HealthUiState.Companion.EMPTY,
            weekDates = (0..6).map { LocalDate.now().plusDays(it.toLong()) },
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
