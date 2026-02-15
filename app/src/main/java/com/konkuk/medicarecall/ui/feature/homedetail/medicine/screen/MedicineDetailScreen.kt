package com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen

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
import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.component.DateSelector
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.common.component.WeeklyCalendar
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.component.MedicineDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicineDetailScreen(
    elderId: Long,
    onBack: () -> Unit,
    viewModel: MedicineViewModel = koinViewModel(),
) {
    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetToToday()
    }

    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        elderId.let { viewModel.loadMedicinesForDate(it, selectedDate) }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    MedicineDetailScreenLayout(
        onBack = onBack,
        selectedDate = selectedDate,
        medicines = state.medicines,
        weekDates = viewModel.getCurrentWeekDates(),
        onDateSelected = { viewModel.selectDate(it) },
        onMonthClick = { /* 모달 열기 */ },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicineDetailScreenLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    medicines: List<MedicineUiState>,
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
                .background(MediCareCallTheme.colors.bg)
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            TopAppBar(
                title = "복약",
                onBack = onBack,
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier
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
                medicines.forEach { uiState ->
                    val medicine = uiState.medicine
                    MedicineDetailCard(
                        medicineName = uiState.medicine.medicineName,
                        todayRequiredCount = uiState.medicine.todayRequiredCount ?: 0,
                        doseStatusList = uiState.displayDoseStatusList,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicineDetailScreen() {
    val dummyMedicines = listOf(
        MedicineUiState(
            medicine = Medicine(
                medicineName = "당뇨약",
                todayRequiredCount = 3,
                doseStatusList = listOf(
                    DoseStatusItem(time = "MORNING", doseStatus = DoseStatus.TAKEN),
                    DoseStatusItem(time = "LUNCH", doseStatus = DoseStatus.SKIPPED),
                ),
            ),
        ),
        MedicineUiState(
            medicine = Medicine(
                medicineName = "혈압약",
                todayRequiredCount = 2,
                doseStatusList = listOf(
                    DoseStatusItem(time = "아침", doseStatus = DoseStatus.TAKEN),
                ),
            ),
        ),
    )

    val today = LocalDate.now()
    MediCareCallTheme {
        MedicineDetailScreenLayout(
            onBack = {},
            selectedDate = today,
            medicines = dummyMedicines,
            weekDates = (0..6).map { today.plus(it, DateTimeUnit.DAY) },
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
