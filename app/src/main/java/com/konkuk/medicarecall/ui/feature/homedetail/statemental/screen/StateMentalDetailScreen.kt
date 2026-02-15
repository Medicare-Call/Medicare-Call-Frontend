package com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen

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
import androidx.compose.material3.Surface
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
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.component.DateSelector
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.common.component.WeeklyCalendar
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.component.StateMentalDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StateMentalDetailScreen(
    elderId: Long,
    onBack: () -> Unit,
    viewModel: MentalViewModel = koinViewModel(),
) {
    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetToToday()
    }

    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val mental by viewModel.mental.collectAsStateWithLifecycle()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        elderId?.let { id ->
            viewModel.loadMentalDataForDate(id, selectedDate)
        }
    }

    StateMentalDetailScreenLayout(
        onBack = onBack,
        selectedDate = selectedDate,
        mental = mental,
        weekDates = viewModel.getCurrentWeekDates(),
        onDateSelected = { viewModel.selectDate(it) },
        onMonthClick = { /* 모달 열기 */ },
    )
}

@Composable
fun StateMentalDetailScreenLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    mental: MentalUiState,
    weekDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            TopAppBar(
                title = "심리상태 요약",
                onBack = onBack,
            )
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
                StateMentalDetailCard(
                    mental = mental,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStateMentalDetailScreen() {
    val today = LocalDate.now()
    StateMentalDetailScreenLayout(
        onBack = {},
        selectedDate = today,
        mental = MentalUiState(),
        weekDates = (0..6).map { today.plus(it, DateTimeUnit.DAY) },
        onDateSelected = {},
        onMonthClick = {},
    )
}
