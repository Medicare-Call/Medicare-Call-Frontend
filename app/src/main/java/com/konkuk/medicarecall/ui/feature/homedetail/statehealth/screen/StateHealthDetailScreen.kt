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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.domain.util.getCurrentWeekDates
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.component.DateSelector
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.common.component.WeeklyCalendar
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.component.StateHealthDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun StateHealthDetailScreen(
    elderId: Long,
    onBack: () -> Unit,
    viewModel: HealthViewModel = koinViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetToToday()
    }

    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val health by viewModel.health.collectAsStateWithLifecycle()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        viewModel.loadHealthDataForDate(elderId, selectedDate)
    }

    if (!isLoading)
        StateHealthDetailScreenLayout(
            modifier = Modifier,
            onBack = onBack,
            selectedDate = selectedDate,
            health = health,
            weekDates = selectedDate.getCurrentWeekDates(),
            onDateSelected = { viewModel.selectDate(it) },
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
fun StateHealthDetailScreenLayout(
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
                weekDates = weekDates,
                selectedDate = selectedDate,
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
fun PreviewStateHealthDetailScreen() {
    MediCareCallTheme {
        val today = LocalDate.now()
        StateHealthDetailScreenLayout(
            onBack = {},
            selectedDate = today,
            health = HealthUiState(),
            weekDates = today.getCurrentWeekDates(),
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
