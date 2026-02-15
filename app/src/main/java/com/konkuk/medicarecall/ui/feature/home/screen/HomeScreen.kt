package com.konkuk.medicarecall.ui.feature.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.domain.model.HomeSleep
import com.konkuk.medicarecall.ui.common.component.NameBar
import com.konkuk.medicarecall.ui.common.component.NameDropdown
import com.konkuk.medicarecall.ui.feature.home.component.CareCallFloatingButton
import com.konkuk.medicarecall.ui.feature.home.component.CareCallSnackBar
import com.konkuk.medicarecall.ui.feature.home.component.HomeGlucoseLevelContainer
import com.konkuk.medicarecall.ui.feature.home.component.HomeMealContainer
import com.konkuk.medicarecall.ui.feature.home.component.HomeMedicineContainer
import com.konkuk.medicarecall.ui.feature.home.component.HomeSleepContainer
import com.konkuk.medicarecall.ui.feature.home.component.HomeStateHealthContainer
import com.konkuk.medicarecall.ui.feature.home.component.HomeStateMentalContainer
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    navigateToMealDetailScreen: (Long) -> Unit,
    navigateToMedicineDetailScreen: (Long) -> Unit,
    navigateToSleepDetailScreen: (Long) -> Unit,
    navigateToStateHealthDetailScreen: (Long) -> Unit,
    navigateToStateMentalDetailScreen: (Long) -> Unit,
    navigateToGlucoseDetailScreen: (Long) -> Unit,
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val elderInfoList by viewModel.elderInfoList.collectAsStateWithLifecycle()
    val elderNameList by viewModel.elderNameList.collectAsStateWithLifecycle()
    val selectedElderId by viewModel.selectedElderId.collectAsStateWithLifecycle()

    var dropdownOpened by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 네비게이션 결과
    val updatedName by viewModel.updatedName.collectAsStateWithLifecycle()

    LaunchedEffect(updatedName) {
        updatedName?.let {
            viewModel.overrideName(it)
            // mainBackStackEntry.savedStateHandle.remove<String>("ELDER_NAME_UPDATED") // 원샷 처리
            viewModel.clearUpdatedName()
        }
    }

    HomeScreenLayout(
        modifier = modifier,
        homeUiState = homeUiState,
        elderInfoList = elderInfoList,
        selectedElderId = selectedElderId,
        isRefreshing = isRefreshing,
        dropdownOpened = dropdownOpened,
        onDropdownClick = { dropdownOpened = true },
        onDropdownDismiss = { dropdownOpened = false },
        onDropdownItemSelected = { selectedName ->
            viewModel.selectElder(selectedName)
            dropdownOpened = false
        },
        navigateToMealDetailScreen = { if (selectedElderId != -1L) navigateToMealDetailScreen(selectedElderId) },
        navigateToMedicineDetailScreen = { if (selectedElderId != -1L) navigateToMedicineDetailScreen(selectedElderId) },
        navigateToSleepDetailScreen = { if (selectedElderId != -1L) navigateToSleepDetailScreen(selectedElderId) },
        navigateToStateHealthDetailScreen = { if (selectedElderId != -1L) navigateToStateHealthDetailScreen(selectedElderId) },
        navigateToStateMentalDetailScreen = { if (selectedElderId != -1L) navigateToStateMentalDetailScreen(selectedElderId) },
        navigateToGlucoseDetailScreen = { if (selectedElderId != -1L) navigateToGlucoseDetailScreen(selectedElderId) },

        navigateToAlarm = {},

        snackbarHostState = snackbarHostState,
        isLoading = homeUiState.isLoading,
        onFabClick = {
            scope.launch {
                snackbarHostState.showSnackbar("케어콜이 곧 연결됩니다. 잠시만 기다려 주세요.")
                delay(3000) // 케어콜 데이터 처리 기다리는 시간
                viewModel.forceRefreshHomeData()
            }
        },
        onRefresh = {
            viewModel.forceRefreshHomeData()
        },
        immediateCall = {
            viewModel.callImmediate(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    elderInfoList: List<ElderInfo>,
    selectedElderId: Long,
    isRefreshing: Boolean,
    dropdownOpened: Boolean,
    onDropdownClick: () -> Unit,
    onDropdownDismiss: () -> Unit,
    onDropdownItemSelected: (String) -> Unit,
    navigateToMealDetailScreen: () -> Unit,
    navigateToMedicineDetailScreen: () -> Unit,
    navigateToSleepDetailScreen: () -> Unit,
    navigateToStateHealthDetailScreen: () -> Unit,
    navigateToStateMentalDetailScreen: () -> Unit,
    navigateToGlucoseDetailScreen: () -> Unit,
    navigateToAlarm: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean,
    onFabClick: () -> Unit,
    onRefresh: () -> Unit,
    immediateCall: (String) -> Unit,
) {
    val elderNameList = remember(elderInfoList) {
        elderInfoList.map { it.name }
    }
    val refreshState = rememberPullToRefreshState()
    val selectedElderName =
        elderInfoList.find { it.elderId == selectedElderId }?.name
            ?.takeIf { it.isNotBlank() }
            ?: homeUiState.elderName
                .takeIf { it.isNotBlank() }
            ?: "어르신 선택"
    var expanded by remember { mutableStateOf(false) }

    val hasSummaryData = homeUiState.balloonMessage.isNotBlank()
    val cardBackgroundColor = if (hasSummaryData) {
        // 데이터 있음 -> 초록색
        MediCareCallTheme.colors.main
    } else {
        // 데이터 없음 (미기록) -> 회색
        MediCareCallTheme.colors.gray3
    }

    val summaryTitleColor =
        if (hasSummaryData) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.white
    val summaryBodyColor =
        if (hasSummaryData) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.white
    val summaryText = if (hasSummaryData) homeUiState.balloonMessage else "아직 기록되지 않았어요."

    Scaffold(

        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
            ) {
                // 세부 FAB들
                AnimatedVisibility(visible = expanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CareCallFloatingButton(
                            modifier = modifier,
                            onClick = {
                                onFabClick()
                                immediateCall("FIRST")
                            },
                            careCallOption = "FIRST",
                            text = "1차",
                        )
                        CareCallFloatingButton(
                            modifier = modifier,
                            onClick = {
                                onFabClick()
                                immediateCall("SECOND")
                            },
                            careCallOption = "SECOND",
                            text = "2차",
                        )
                        CareCallFloatingButton(
                            modifier = modifier,
                            onClick = {
                                onFabClick()
                                immediateCall("THIRD")
                            },
                            careCallOption = "THIRD",
                            text = "3차",
                        )
                    }
                }

                // 메인 FAB
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = MediCareCallTheme.colors.main,
                    contentColor = MediCareCallTheme.colors.white,
                    shape = CircleShape,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_carecall),
                        contentDescription = "메인 FAB",
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.offset(y = -(10).dp),
            ) { data ->
                CareCallSnackBar(snackBarData = data)
            }
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding),
            ) {
                NameBar(
                    name = selectedElderName,
                    modifier = Modifier.statusBarsPadding(),
                    onDropdownClick = onDropdownClick,
                    notificationCount = homeUiState.unreadNotification ?: 0,
                    navigateToAlarm = navigateToAlarm,
                )
                val scope = rememberCoroutineScope()

                PullToRefreshBox(
                    isRefreshing,
                    {
                        scope.launch {
                            onRefresh()
                            refreshState.animateToHidden()
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MediCareCallTheme.colors.bg),
                    state = refreshState,
                    indicator = {
                        // 기본 인디케이터를 사용하되, 색상과 높이만 변경
                        PullToRefreshDefaults.Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state = refreshState,
                            color = MediCareCallTheme.colors.main,
                            containerColor = MediCareCallTheme.colors.white,
                        )
                    },
                ) {
                    when (isLoading) {
                        true -> Box(
                            Modifier
                                .fillMaxSize(),
                        ) {
                            CircularProgressIndicator(
                                color = MediCareCallTheme.colors.main,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }

                        false -> Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                        ) {
                            Spacer(Modifier.height(20.dp))

                            // 오늘의 건강 통계
                            Text(
                                text = "오늘의 건강 통계",
                                style = MediCareCallTheme.typography.SB_18,
                                color = MediCareCallTheme.colors.gray6,
                            )

                            Spacer(Modifier.height(20.dp))

                            // 초록색 한 줄 요약 카드
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.char_medi),
                                            contentDescription = "요약 아이콘",
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "한 줄 요약",
                                            style = MediCareCallTheme.typography.B_20,
                                            color = summaryTitleColor,
                                        )
                                    }
                                    Spacer(Modifier.height(30.dp))
                                    Text(
                                        text = summaryText,
                                        style = MediCareCallTheme.typography.R_16,
                                        color = summaryBodyColor,
                                    )
                                }
                            }

                            // 건강 항목별 상세 카드
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Spacer(Modifier.height(30.dp))
                                HomeMealContainer(
                                    breakfastEaten = homeUiState.breakfastEaten,
                                    lunchEaten = homeUiState.lunchEaten,
                                    dinnerEaten = homeUiState.dinnerEaten,
                                    onClick = navigateToMealDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeMedicineContainer(
                                    medicines = homeUiState.medicines,
                                    onClick = navigateToMedicineDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                                val sleepData = homeUiState.sleep
                                HomeSleepContainer(
                                    totalSleepHours = sleepData?.totalSleepHours ?: 0,
                                    totalSleepMinutes = sleepData?.totalSleepMinutes ?: 0,
                                    isRecorded = (sleepData?.totalSleepHours ?: 0) > 0 || (sleepData?.totalSleepMinutes ?: 0) > 0,
                                    onClick = navigateToSleepDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeStateHealthContainer(
                                    healthStatus = homeUiState.healthStatus,
                                    onClick = navigateToStateHealthDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeStateMentalContainer(
                                    mentalStatus = homeUiState.mentalStatus,
                                    onClick = navigateToStateMentalDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeGlucoseLevelContainer(
                                    glucoseLevelAverageToday = homeUiState.glucoseLevelAverageToday,
                                    onClick = navigateToGlucoseDetailScreen,
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }

        if (dropdownOpened) {
            NameDropdown(
                items = elderNameList,
                selectedName = selectedElderName,
                onDismiss = onDropdownDismiss,
                onItemSelected = onDropdownItemSelected,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
fun PreviewHomeScreen() {
    val previewUiState = HomeUiState(
        elderName = "김옥자",
        balloonMessage = "아침·점심 복약과 식사는 문제 없으나, 저녁 약 복용이 늦어질 우려가 있어요.",
        breakfastEaten = true,
        lunchEaten = false,
        dinnerEaten = null,
        medicines = listOf(
            MedicineUiState("혈압약", 2, 3, "저녁"),
            MedicineUiState("당뇨약", 1, 2, "저녁"),
        ),
        sleep = HomeSleep(totalSleepHours = 8, totalSleepMinutes = 15, isRecorded = true),
        healthStatus = "좋음",
        mentalStatus = "좋음",
        glucoseLevelAverageToday = 120,
    )

    val previewElderInfoList = listOf(
        ElderInfo(elderId = 1L, name = "김옥자", phone = "010-1111-1111"),
        ElderInfo(elderId = 2L, name = "박막례", phone = "010-2222-2222"),
        ElderInfo(elderId = 3L, name = "최이순", phone = "010-3333-3333"),
    )
    val previewSelectedId = 1L

    MediCareCallTheme {
        HomeScreenLayout(
            homeUiState = previewUiState,
            elderInfoList = previewElderInfoList,
            selectedElderId = previewSelectedId,
            isRefreshing = false,
            dropdownOpened = false,
            onDropdownClick = {},
            onDropdownDismiss = {},
            onDropdownItemSelected = {},
            navigateToMealDetailScreen = {},
            navigateToMedicineDetailScreen = {},
            navigateToSleepDetailScreen = {},
            navigateToStateHealthDetailScreen = {},
            navigateToStateMentalDetailScreen = {},
            navigateToGlucoseDetailScreen = {},
            snackbarHostState = SnackbarHostState(),
            isLoading = false,
            immediateCall = {},
            onRefresh = {},
            onFabClick = {},
        )
    }
}

@Preview(showBackground = true, name = "홈 화면 (미기록 상태)", heightDp = 1500)
@Composable
fun PreviewHomeScreenUnrecorded() {
    val unrecordedUiState = HomeUiState(
        isLoading = false,
        elderName = "김옥자",
        balloonMessage = "",
        breakfastEaten = null,
        lunchEaten = null,
        dinnerEaten = null,
        medicines = emptyList(),
        sleep = HomeSleep(totalSleepHours = 0, totalSleepMinutes = 0, isRecorded = false),
        healthStatus = "",
        mentalStatus = "",
        glucoseLevelAverageToday = 0,
    )

    val previewElderInfoList = listOf(
        ElderInfo(elderId = 1L, name = "김옥자", phone = "010-1111-1111"),
        ElderInfo(elderId = 2L, name = "박막례", phone = "010-2222-2222"),
    )
    val previewSelectedId = 1L

    MediCareCallTheme {
        HomeScreenLayout(
            homeUiState = unrecordedUiState,
            elderInfoList = previewElderInfoList,
            selectedElderId = previewSelectedId,
            isRefreshing = false,
            dropdownOpened = false,
            onDropdownClick = {},
            onDropdownDismiss = {},
            onDropdownItemSelected = {},
            snackbarHostState = SnackbarHostState(),
            isLoading = false,
            immediateCall = {},
            onRefresh = {},
            onFabClick = {},
            navigateToMealDetailScreen = { },
            navigateToMedicineDetailScreen = { },
            navigateToSleepDetailScreen = { },
            navigateToStateHealthDetailScreen = { },
            navigateToStateMentalDetailScreen = { },
            navigateToGlucoseDetailScreen = { },
            navigateToAlarm = { },
        )
    }
}
