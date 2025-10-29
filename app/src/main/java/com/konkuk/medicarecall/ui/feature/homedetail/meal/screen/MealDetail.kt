package com.konkuk.medicarecall.ui.feature.homedetail.meal.screen

import android.util.Log
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
import com.konkuk.medicarecall.ui.feature.homedetail.meal.component.MealDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealUiState
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealDetail(
    onBack: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    mealViewModel: MealViewModel = hiltViewModel(),
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        calendarViewModel.resetToToday()
    }

    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val elderId by homeViewModel.selectedElderId.collectAsState()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        Log.d("MED_UI", "LaunchedEffect: elderId=$elderId, date=$selectedDate")
        elderId?.let { mealViewModel.loadMealsForDate(it, selectedDate) }
    }

    val meals by mealViewModel.meals.collectAsState()

    MealDetailLayout(
        onBack = onBack,
        selectedDate = selectedDate,
        meals = meals,
        weekDates = calendarViewModel.getCurrentWeekDates(),
        onDateSelected = { calendarViewModel.selectDate(it) },
        onMonthClick = { /* 모달 열기 */ },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealDetailLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    meals: List<MealUiState>,
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
                title = "식사",
                onBack = onBack,
            )
            Spacer(modifier = Modifier.height(4.dp))
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
                    calendarUiState = CalendarUiState(
                        currentYear = selectedDate.year,
                        currentMonth = selectedDate.monthValue,
                        weekDates = weekDates,
                        selectedDate = selectedDate,
                    ),
                    onDateSelected = onDateSelected,
                )

                Spacer(modifier = Modifier.height(32.dp))

                meals.forEach { meal ->
                    MealDetailCard(
                        mealTime = meal.mealTime, // 아침 점심 저녁
                        description = meal.description, // 식사 내용
                        isRecorded = meal.isRecorded, // 식사 기록 여부
                        isEaten = meal.isEaten, // 식사 유무
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(name = "식사 - 기록 있음", showBackground = true)
@Composable
fun PreviewMealDetail_Recorded() {
    val dummyMeals = listOf(
        MealUiState(
            mealTime = "아침",
            description = "간단히 밥과 반찬을 드셨어요.",
            isRecorded = true,
            isEaten = true,
        ),
        MealUiState(
            mealTime = "점심",
            description = "식사하지 않으셨어요.",
            isRecorded = true,
            isEaten = false,
        ),
        MealUiState(
            mealTime = "저녁",
            description = "죽을 드셨어요.",
            isRecorded = true,
            isEaten = true,
        ),
    )
    val selectedDate = LocalDate.of(2025, 5, 7)
    val weekDates =
        (0..6).map { selectedDate.plusDays(it.toLong() - selectedDate.dayOfWeek.value % 7) }

    MediCareCallTheme {
        MealDetailLayout(
            onBack = {},
            selectedDate = selectedDate,
            meals = dummyMeals,
            weekDates = weekDates,
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}

@Preview(name = "식사 - 미기록 화면", showBackground = true)
@Composable
fun PreviewMealDetail_Unrecorded() {
    val dummyMeals = listOf(
        MealUiState(
            mealTime = "아침",
            description = "식사 기록 전이에요.",
            isRecorded = false,
            isEaten = null,
        ),
        MealUiState(
            mealTime = "점심",
            description = "식사 기록 전이에요.",
            isRecorded = false,
            isEaten = null,
        ),
        MealUiState(
            mealTime = "저녁",
            description = "식사 기록 전이에요.",
            isRecorded = false,
            isEaten = null,
        ),
    )
    val selectedDate = LocalDate.of(2025, 5, 7)
    val weekDates =
        (0..6).map { selectedDate.plusDays(it.toLong() - selectedDate.dayOfWeek.value % 7) }

    MediCareCallTheme {
        MealDetailLayout(
            onBack = {},
            selectedDate = selectedDate,
            meals = dummyMeals,
            weekDates = weekDates,
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
