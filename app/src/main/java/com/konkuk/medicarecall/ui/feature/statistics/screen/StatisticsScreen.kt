package com.konkuk.medicarecall.ui.feature.statistics.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.component.NameBar
import com.konkuk.medicarecall.ui.common.component.NameDropdown
import com.konkuk.medicarecall.ui.feature.alarm.navigation.navigateToAlarm
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.statistics.component.WeekendBar
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.StatisticsUiState
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.StatisticsViewModel
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState.WeeklyMedicineUiState
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklyGlucoseCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklyHealthCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklyMealCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklyMedicineCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklyMentalCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklySleepCard
import com.konkuk.medicarecall.ui.feature.statistics.weeklycard.WeeklySummaryCard
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.DateTimeUnit
import org.koin.androidx.compose.koinViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToAlarm: () -> Unit = {},
    homeViewModel: HomeViewModel = koinViewModel(),
    statisticsViewModel: StatisticsViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = true) {
        statisticsViewModel.refresh()
    }

    // 화면 복귀 시 자동 새로고침
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                statisticsViewModel.refresh() // ← 현재 elderId + 현재 주 기준으로 재조회
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val uiState by statisticsViewModel.uiState.collectAsStateWithLifecycle()

    // ① HomeVM에서 어르신 전체 목록과 이름 목록을 가져옵니다.
    val eldersMap = uiState.eldersMap
    val elderNameList = eldersMap.values.toList()

    // ② 선택된 어르신의 ID를 가져옵니다.
    val selectedElderId by homeViewModel.selectedElderId.collectAsStateWithLifecycle()

    // ③ 통계 VM의 상태를 구독합니다.
    val currentWeek = uiState.currentWeek
    val isLatestWeek = uiState.isLatestWeek
    val isEarliestWeek = uiState.isEarliestWeek

    // ④ 표시할 이름을 결정합니다.
    // 우선순위 1: 통계 데이터에 포함된 이름 (가장 정확함)
    // 우선순위 2: ID를 통해 전체 목록에서 찾은 이름 (로딩 중일 때 표시)
    // 우선순위 3: 목록의 첫 번째 이름 (초기 상태)
    val currentElderName = remember(uiState.summary, eldersMap, selectedElderId) {
        eldersMap[selectedElderId]
            ?: uiState.summary?.elderName?.takeIf { it.isNotEmpty() }
            ?: elderNameList.firstOrNull()
            ?: "어르신 통계"
    }

    // elderId가 바뀌면 통계 VM에 알려줍니다.
    LaunchedEffect(selectedElderId) {
        if (selectedElderId != -1L) {
            statisticsViewModel.setSelectedElderId(selectedElderId)
        }
    }

    // 복약 변경 이벤트 수신
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("medsChanged", false)
            ?.collect { changed ->
                if (changed) {
                    statisticsViewModel.onMedsChanged()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("medsChanged", false)
                }
            }
    }

    StatisticsScreenLayout(
        modifier = modifier,
        uiState = uiState,
        elderNameList = elderNameList,
        currentWeek = currentWeek,
        isLatestWeek = isLatestWeek,
        isEarliestWeek = isEarliestWeek,
        onPreviousWeek = { statisticsViewModel.showPreviousWeek() },
        onNextWeek = { statisticsViewModel.showNextWeek() },
        onDropdownItemSelected = { name -> homeViewModel.selectElder(name) },
        currentElderName = currentElderName,
        navigateToAlarm = { navController.navigateToAlarm() },
    )
}

@Composable
fun StatisticsScreenLayout(
    modifier: Modifier = Modifier,
    uiState: StatisticsUiState,
    elderNameList: List<String>,
    navigateToAlarm: () -> Unit = {},
    currentWeek: Pair<LocalDate, LocalDate>,
    isLatestWeek: Boolean,
    isEarliestWeek: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onDropdownItemSelected: (String) -> Unit,
    currentElderName: String,
) {
    val dropdownOpened = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.white),
    ) {
        NameBar(
            name = currentElderName,
            modifier = Modifier.statusBarsPadding(),
            onDropdownClick = { dropdownOpened.value = !dropdownOpened.value },
            notificationCount = uiState.summary?.unreadNotification ?: 0,
            navigateToAlarm = navigateToAlarm,
        )

        when {
            uiState.isLoading -> {
                Box(
                    Modifier.fillMaxSize(),
                ) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center),
                        color = MediCareCallTheme.colors.main,
                    )
                }
            }

            uiState.error != null -> { /* ... */
            }

            uiState.summary != null -> {
                WeekendBar(
                    currentWeek = currentWeek,
                    isLatestWeek = isLatestWeek,
                    isEarliestWeek = isEarliestWeek,
                    onPreviousWeek = onPreviousWeek,
                    onNextWeek = onNextWeek,
                )
                Column {
                    StatisticsContent(
                        summary = uiState.summary,
                    )
                }
            }
        }
    }

    if (dropdownOpened.value) {
        NameDropdown(
            items = elderNameList,
            selectedName = currentElderName,
            onDismiss = { dropdownOpened.value = false },
            onItemSelected = { name ->
                onDropdownItemSelected(name)
                dropdownOpened.value = false
            },
        )
    }
}

@Composable
private fun StatisticsContent(
    summary: WeeklySummaryUiState,
) {
    Column(
        modifier = Modifier
            .background(MediCareCallTheme.colors.bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        WeeklySummaryCard(
            summary = summary,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            WeeklyMealCard(
                modifier = Modifier
                    .fillMaxHeight(),
                meal = summary.weeklyMeals,
            )
            WeeklyMedicineCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                medicine = summary.weeklyMedicines,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyHealthCard(
            healthNote = summary.weeklyHealthNote,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            WeeklySleepCard(
                modifier = Modifier.weight(1f),
                summary = summary,
            )
            WeeklyMentalCard(
                modifier = Modifier.weight(1f),
                mental = summary.weeklyMental,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyGlucoseCard(
            weeklyGlucose = summary.weeklyGlucose,
        )
        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Preview(name = "주간 통계 - 기록 있음", showBackground = true, heightDp = 1200)
@Composable
fun PreviewStatisticsScreenRecorded() {
    val dummySummary = WeeklySummaryUiState(
        elderName = "김옥자",
        weeklyMealRate = 65,
        weeklyMedicineRate = 57,
        weeklyHealthIssueCount = 3,
        weeklyUnansweredCount = 8,
        weeklyMeals = listOf(
            WeeklySummaryUiState.WeeklyMealUiState("아침", 7, 7),
            WeeklySummaryUiState.WeeklyMealUiState("점심", 5, 7),
            WeeklySummaryUiState.WeeklyMealUiState("저녁", 1, 7),
        ),
        weeklyMedicines = listOf(
            WeeklyMedicineUiState("혈압약", 0, 14),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("당뇨약", 21, 21),
        ),

        weeklyHealthNote = "아침·점심 복약과 식사는 문제 없으나, 저녁 약 복용이 늦어질 우려가 있어요. 전반적으로 양호하나 피곤과 후흡곤란을 호소하셨으므로 휴식과 보호자 확인이 필요해요.",
        weeklySleepHours = 7,
        weeklySleepMinutes = 12,
        weeklyMental = WeeklySummaryUiState.WeeklyMentalUiState(good = 4, normal = 4, bad = 1),
        weeklyGlucose = WeeklySummaryUiState.WeeklyGlucoseUiState(
            beforeMealNormal = 5,
            beforeMealHigh = 2,
            beforeMealLow = 1,
            afterMealNormal = 5,
            afterMealHigh = 0,
            afterMealLow = 2,
        ),
    )
    val dummyUiState = StatisticsUiState(summary = dummySummary)

    MediCareCallTheme {
        StatisticsScreenLayout(
            uiState = dummyUiState,
            elderNameList = listOf("김옥자", "박막례"),
            currentWeek = Pair(
                LocalDate.now(),
                LocalDate.now().plus(6, DateTimeUnit.DAY),
            ),
            isLatestWeek = false,
            isEarliestWeek = false,
            onPreviousWeek = {},
            onNextWeek = {},
            onDropdownItemSelected = {},
            currentElderName = "김옥자",
        )
    }
}

@Preview(name = "주간 통계 - 미기록", showBackground = true, heightDp = 1200)
@Composable
fun PreviewStatisticsScreenUnrecorded() {
    MediCareCallTheme {
        StatisticsScreenLayout(
            uiState = StatisticsUiState(
                summary = WeeklySummaryUiState(elderName = "김옥자"),
            ),
            elderNameList = listOf("김옥자", "박막례"),
            currentWeek = Pair(LocalDate.now(), LocalDate.now().plus(6, DateTimeUnit.DAY)),
            isLatestWeek = true,
            isEarliestWeek = true,
            onPreviousWeek = {},
            onNextWeek = {},
            onDropdownItemSelected = {},
            currentElderName = "김옥자",
        )
    }
}
