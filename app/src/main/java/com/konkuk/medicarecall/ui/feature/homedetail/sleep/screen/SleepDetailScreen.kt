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
import androidx.compose.runtime.getValue
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
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.component.SleepDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepUiState
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SleepDetailScreen(
    onBack: () -> Unit,
    viewModel: SleepViewModel = koinViewModel(),
) {
    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetToToday()
    }

    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(selectedDate) {
        viewModel.loadSleepDataForDate(selectedDate)
    }

    SleepDetailScreenLayout(
        modifier = Modifier,
        onBack = onBack,
        selectedDate = selectedDate,
        weekDates = selectedDate.getCurrentWeekDates(),
        onDateSelected = { viewModel.selectDate(it) },
        sleep = uiState,
        onMonthClick = { /* 모달 열기 */ },
    )
}

@Suppress("detekt:UnusedParameter") // TODO: detekt 무시하는 주석, 이후에 삭제할 것
@Composable
fun SleepDetailScreen(
    modifier: Modifier = Modifier,
    sleep: SleepUiState = SleepUiState(),
    onBack: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onMonthClick: () -> Unit = {},
) {
    // TODO: UI ...
}

// TODO: 나중에 제거
@Composable
fun SleepDetailScreenLayout(
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
                weekDates = weekDates,
                selectedDate = selectedDate,
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
fun PreviewSleepDetailScreen() {
    MediCareCallTheme {
        val today = LocalDate.now()
        SleepDetailScreenLayout(
            onBack = {},
            selectedDate = today,
            sleep = SleepUiState(),
            weekDates = today.getCurrentWeekDates(),
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
