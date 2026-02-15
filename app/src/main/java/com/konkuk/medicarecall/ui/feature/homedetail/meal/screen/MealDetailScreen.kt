package com.konkuk.medicarecall.ui.feature.homedetail.meal.screen

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
import com.konkuk.medicarecall.domain.model.Meal
import com.konkuk.medicarecall.ui.common.component.DateSelector
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.common.component.WeeklyCalendar
import com.konkuk.medicarecall.ui.feature.homedetail.meal.component.MealDetailCard
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealDetailScreen(
    elderId: Long,
    onBack: () -> Unit,
    viewModel: MealViewModel = koinViewModel(),
) {
    // 재진입 시 오늘로 초기화
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.resetToToday()
    }
    // 날짜만 Observe
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val uiState by viewModel.meals.collectAsStateWithLifecycle()

    // 날짜/어르신 변경 시마다 로드
    LaunchedEffect(elderId, selectedDate) {
        viewModel.loadMealsForDate(elderId, selectedDate)
    }

    MealDetailScreenLayout(
        onBack = onBack,
        selectedDate = selectedDate,
        meals = uiState.meals,
        weekDates = viewModel.getCurrentWeekDates(),
        onDateSelected = viewModel::selectDate,
        onMonthClick = { /* 모달 열기 */ },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealDetailScreenLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    selectedDate: LocalDate,
    meals: List<Meal>,
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
                    weekDates = weekDates,
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected,
                )

                Spacer(modifier = Modifier.height(32.dp))

                val times = listOf("아침", "점심", "저녁")

                times.forEach { time ->
                    val meal = meals.find { it.mealTime == time }

                    val isRecorded = !meal?.description.isNullOrBlank()

                    MealDetailCard(
                        mealTime = time,
                        description = meal?.description.orEmpty(),
                        isRecorded = isRecorded,
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(name = "식사 - 기록 있음", showBackground = true)
@Composable
fun PreviewMealDetailScreenRecorded() {
    val selectedDate = LocalDate(2025, 5, 7)
    val weekDates = (0..6).map { selectedDate.plus(it, DateTimeUnit.DAY) }

    val dummyMeals = listOf(
        Meal(mealTime = "아침", description = "간단히 밥과 반찬을 드셨어요."),
        Meal(mealTime = "점심", description = null),
        Meal(mealTime = "저녁", description = "죽을 드셨어요."),
    )

    MediCareCallTheme {
        MealDetailScreenLayout(
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
fun PreviewMealDetailScreenUnrecorded() {
    val selectedDate = LocalDate(2025, 5, 7)
    val startOfWeek = selectedDate.plus(-(selectedDate.dayOfWeek.ordinal % 7), DateTimeUnit.DAY)
    val weekDates = (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }

    val dummyMeals = listOf(
        Meal(
            mealTime = "아침",
            description = "식사 기록 전이에요.",
        ),
        Meal(
            mealTime = "점심",
            description = "식사 기록 전이에요.",
        ),
        Meal(
            mealTime = "저녁",
            description = "식사 기록 전이에요.",
        ),
    )

    MediCareCallTheme {
        MealDetailScreenLayout(
            onBack = {},
            selectedDate = selectedDate,
            meals = dummyMeals,
            weekDates = weekDates,
            onDateSelected = {},
            onMonthClick = {},
        )
    }
}
