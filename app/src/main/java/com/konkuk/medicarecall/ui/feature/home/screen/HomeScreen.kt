package com.konkuk.medicarecall.ui.feature.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.NameBar
import com.konkuk.medicarecall.ui.common.component.NameDropdown
import com.konkuk.medicarecall.ui.feature.home.component.*
import com.konkuk.medicarecall.ui.feature.home.viewmodel.ElderInfo
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
    navigateToMealDetailScreen: (Int) -> Unit,
    navigateToMedicineDetailScreen: (Int) -> Unit,
    navigateToSleepDetailScreen: (Int) -> Unit,
    navigateToStateHealthDetailScreen: (Int) -> Unit,
    navigateToStateMentalDetailScreen: (Int) -> Unit,
    navigateToGlucoseDetailScreen: (Int) -> Unit,
    mainBackStackEntry: NavBackStackEntry,
) {
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    val elderInfoList by homeViewModel.elderInfoList.collectAsStateWithLifecycle()
    val elderNameList by homeViewModel.elderNameList.collectAsStateWithLifecycle()
    val selectedElderId by homeViewModel.selectedElderId.collectAsStateWithLifecycle()
    val isInitialLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()

    var dropdownOpened by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val updatedName by mainBackStackEntry.savedStateHandle
        .getStateFlow<String?>("ELDER_NAME_UPDATED", null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(updatedName) {
        updatedName?.let {
            homeViewModel.overrideName(it)
            mainBackStackEntry.savedStateHandle.remove<String>("ELDER_NAME_UPDATED") // 원샷 처리
        }
    }

    HomeScreenLayout(
        modifier = modifier,
        homeUiState = homeUiState,
        elderInfoList = elderInfoList,
        elderNameList = elderNameList,
        selectedElderId = selectedElderId,
        isRefreshing = isRefreshing,
        dropdownOpened = dropdownOpened,
        onDropdownClick = { dropdownOpened = true },
        onDropdownDismiss = { dropdownOpened = false },
        onDropdownItemSelected = { selectedName ->
            homeViewModel.selectElder(selectedName)
            dropdownOpened = false
        },
        navigateToMealDetailScreen = { selectedElderId?.let(navigateToMealDetailScreen) },
        navigateToMedicineDetailScreen = { selectedElderId?.let(navigateToMedicineDetailScreen) },
        navigateToSleepDetailScreen = { selectedElderId?.let(navigateToSleepDetailScreen) },
        navigateToStateHealthDetailScreen = { selectedElderId?.let(navigateToStateHealthDetailScreen) },
        navigateToStateMentalDetailScreen = { selectedElderId?.let(navigateToStateMentalDetailScreen) },
        navigateToGlucoseDetailScreen = { selectedElderId?.let(navigateToGlucoseDetailScreen) },
        snackbarHostState = snackbarHostState,
        isLoading = isInitialLoading || homeUiState.isLoading,
        onFabClick = {
            scope.launch {
                snackbarHostState.showSnackbar("케어콜이 곧 연결됩니다. 잠시만 기다려 주세요.")
                delay(3000)
                homeViewModel.forceRefreshHomeData()
            }
        },
        onRefresh = {
            isRefreshing = true
            homeViewModel.forceRefreshHomeData {
                isRefreshing = false
            }
        },
        immediateCall = {
            homeViewModel.callImmediate(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    elderInfoList: List<ElderInfo>,
    elderNameList: List<String>,
    selectedElderId: Int?,
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
    val refreshState = rememberPullToRefreshState()
    val selectedElderName = elderInfoList.find { it.id == selectedElderId }?.name
        ?: homeUiState.elderName.takeIf { it.isNotBlank() } ?: "어르신 선택"

    var expanded by remember { mutableStateOf(false) }

    val hasSummaryData = homeUiState.balloonMessage.isNotBlank()
    val cardBackgroundColor = if (hasSummaryData) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3
    val summaryTextColor = if (hasSummaryData) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.white
    val summaryText = if (hasSummaryData) homeUiState.balloonMessage else "아직 기록되지 않았어요."

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
            ) {
                AnimatedVisibility(visible = expanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        listOf("FIRST" to "1차", "SECOND" to "2차", "THIRD" to "3차").forEach { (opt, txt) ->
                            CareCallFloatingButton(
                                onClick = { onFabClick(); immediateCall(opt) },
                                careCallOption = opt,
                                text = txt,
                            )
                        }
                    }
                }
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = MediCareCallTheme.colors.main,
                    contentColor = MediCareCallTheme.colors.white,
                    shape = CircleShape,
                ) {
                    Icon(painter = painterResource(R.drawable.ic_carecall), contentDescription = null)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.offset(y = -(10).dp)) { data ->
                CareCallSnackBar(snackBarData = data)
            }
        },
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White).padding(innerPadding),
            ) {
                NameBar(
                    name = selectedElderName,
                    modifier = Modifier.statusBarsPadding(),
                    onDropdownClick = onDropdownClick,
                    notificationCount = homeUiState.unreadNotification ?: 0,
                    navigateToAlarm = navigateToAlarm,
                )

                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize().background(MediCareCallTheme.colors.bg),
                    state = refreshState,
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state = refreshState,
                            color = MediCareCallTheme.colors.main,
                            containerColor = MediCareCallTheme.colors.white,
                        )
                    },
                ) {
                    if (isLoading && !isRefreshing) {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                color = MediCareCallTheme.colors.main,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(horizontal = 20.dp),
                        ) {
                            Spacer(Modifier.height(20.dp))
                            Text(text = "오늘의 건강 통계", style = MediCareCallTheme.typography.SB_18, color = MediCareCallTheme.colors.gray6)
                            Spacer(Modifier.height(20.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.char_medi), contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = "한 줄 요약", style = MediCareCallTheme.typography.B_20, color = summaryTextColor)
                                    }
                                    Spacer(Modifier.height(30.dp))
                                    Text(text = summaryText, style = MediCareCallTheme.typography.R_16, color = summaryTextColor)
                                }
                            }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(Modifier.height(30.dp))
                                HomeMealContainer(
                                    breakfastEaten = homeUiState.breakfastEaten,
                                    lunchEaten = homeUiState.lunchEaten,
                                    dinnerEaten = homeUiState.dinnerEaten,
                                    onClick = navigateToMealDetailScreen
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeMedicineContainer(
                                    medicines = homeUiState.medicines,
                                    onClick = navigateToMedicineDetailScreen
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeSleepContainer(
                                    totalSleepHours = homeUiState.sleep?.meanHours ?: 0,
                                    totalSleepMinutes = homeUiState.sleep?.meanMinutes ?: 0,
                                    isRecorded = (homeUiState.sleep?.meanHours ?: 0) > 0 || (homeUiState.sleep?.meanMinutes ?: 0) > 0,
                                    onClick = navigateToSleepDetailScreen
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeStateHealthContainer(
                                    healthStatus = homeUiState.healthStatus ?: "",
                                    onClick = navigateToStateHealthDetailScreen
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeStateMentalContainer(
                                    mentalStatus = homeUiState.mentalStatus ?: "",
                                    onClick = navigateToStateMentalDetailScreen
                                )
                                Spacer(Modifier.height(12.dp))
                                HomeGlucoseLevelContainer(
                                    glucoseLevelAverageToday = homeUiState.glucoseLevelAverageToday ?: 0,
                                    onClick = navigateToGlucoseDetailScreen
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
