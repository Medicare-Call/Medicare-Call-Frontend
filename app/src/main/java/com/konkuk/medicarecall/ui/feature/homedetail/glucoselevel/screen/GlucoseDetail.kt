package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.TopAppBar
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component.GlucoseGraph
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component.GlucoseListItem
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component.GlucoseStatusItem
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component.GlucoseTimingButton
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel.GlucoseUiState
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel.GlucoseViewModel
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlucoseDetail(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // 어르신 선택 상태(selectedElderId) 관리
    val homeViewModel: HomeViewModel = hiltViewModel()
    val viewModel: GlucoseViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    // 선택된 어르신 ID를 구독 (null 가능)
    val elderId by homeViewModel.selectedElderId.collectAsState()

    val counter = remember {
        mutableStateMapOf(
            GlucoseTiming.BEFORE_MEAL to 0,
            GlucoseTiming.AFTER_MEAL to 0,
        )
    }

    val coroutineScope = rememberCoroutineScope()

    // 로딩 요청 중복 방지를 위한 플래그
    val isRequestingMore = remember { mutableStateOf(false) }

    // 데이터 새로고침 로직
    val refreshData = remember(viewModel) {
        {
            elderId?.let { id ->
                counter[GlucoseTiming.BEFORE_MEAL] = 0
                counter[GlucoseTiming.AFTER_MEAL] = 0
                viewModel.getGlucoseData(id, 0, GlucoseTiming.BEFORE_MEAL, true)
                viewModel.getGlucoseData(id, 0, GlucoseTiming.AFTER_MEAL, true)
            }
        }
    }

    // 어르신이 바뀔 때마다 데이터를 새로고침합니다.
    LaunchedEffect(elderId) {
        refreshData()
    }

    // 화면에 다시 진입할 때마다 데이터를 새로고침합니다.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        refreshData()
    }

    // 무한 스크롤 (더 빠른 트리거와 중복 요청 방지)
    LaunchedEffect(scrollState.value, scrollState.maxValue) {
        Log.d("scroll", "value: ${scrollState.value}, max: ${scrollState.maxValue}, isLoading: ${uiState.isLoading}, hasNext: ${uiState.hasNext}")

        // 더 일찍 트리거 (500dp 전에 미리 로딩)
        val shouldLoad = scrollState.value > scrollState.maxValue - 500 || scrollState.maxValue <= 100

        if (shouldLoad && elderId != null && !uiState.isLoading && uiState.hasNext && !isRequestingMore.value) {
            isRequestingMore.value = true
            val currentTiming = uiState.selectedTiming
            val currentPage = counter.getValue(currentTiming)
            Log.d("scroll", "Loading page ${currentPage + 1} for $currentTiming")
            viewModel.getGlucoseData(elderId!!, currentPage + 1, currentTiming, false)
            counter[currentTiming] = currentPage + 1
        }
    }

    // 로딩 완료 시 플래그 리셋
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRequestingMore.value = false
        }
    }

    GlucoseDetailLayout(
        modifier = modifier,
        uiState = uiState,
        selectedTiming = uiState.selectedTiming,
        selectedIndex = uiState.selectedIndex,

        // '공복'/'식후' 버튼
        onTimingChange = { newTiming ->
            viewModel.updateTiming(newTiming)
            coroutineScope.launch {
                scrollState.scrollTo(0)
            }
        },
        // 그래프 점
        onPointClick = { newIndex -> viewModel.onClickDots(newIndex) },
        scrollState = scrollState,
        onBack = onBack,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlucoseDetailLayout(
    modifier: Modifier = Modifier,
    uiState: GlucoseUiState,
    selectedTiming: GlucoseTiming,
    selectedIndex: Int,
    onTimingChange: (GlucoseTiming) -> Unit,
    onPointClick: (Int) -> Unit,
    scrollState: ScrollState,
    onBack: () -> Unit,
) {
    val isDataAvailable = uiState.graphDataPoints.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.white),
    ) {
        Spacer(
            Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
                .background(Color.White),
        )

        TopAppBar(
            title = "혈당",
            onBack = onBack,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            GlucoseTimingButton(
                modifier = Modifier.weight(1f),
                text = "공복",
                selected = selectedTiming == GlucoseTiming.BEFORE_MEAL,
                onClick = { onTimingChange(GlucoseTiming.BEFORE_MEAL) },
            )
            GlucoseTimingButton(
                modifier = Modifier.weight(1f),
                text = "식후",
                selected = selectedTiming == GlucoseTiming.AFTER_MEAL,
                onClick = { onTimingChange(GlucoseTiming.AFTER_MEAL) },
            )
        }

        if (isDataAvailable) {
            Spacer(modifier = Modifier.height(32.dp))

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GlucoseStatusItem()
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 혈당 그래프
                GlucoseGraph(
                    data = uiState.graphDataPoints,
                    selectedIndex = selectedIndex,
                    onPointClick = onPointClick,
                    scrollState = scrollState,
                    timing = selectedTiming,
                )
                Spacer(modifier = Modifier.height(44.dp))

                // 그래프 점 클릭시
                val selectedPoint = uiState.graphDataPoints.getOrNull(selectedIndex)
                if (selectedPoint != null) {
                    val timingLabel =
                        if (selectedTiming == GlucoseTiming.BEFORE_MEAL) "아침 | 공복" else "저녁 | 식후"

                    // 날짜 표시
//                    val dateText = selectedPoint.date.format(
//                        DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN)
//                    )
                    // 혈당 상세 정보
                    GlucoseListItem(
                        date = selectedPoint.date,
                        timingLabel = timingLabel,
                        value = selectedPoint.value.toInt(),
                        timing = selectedTiming,
                    )
                }
            }
        } else {
            // EMPTY VIEW
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 177.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_record),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp),
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "아직 기록이 없어요",
                    style = MediCareCallTheme.typography.R_18,
                    color = MediCareCallTheme.colors.gray6,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, name = "데이터 있을 때")
@Composable
fun PreviewGlucoseDetail_DataAvailable() {
    // 더미 데이터 프리뷰
    val today = LocalDate.now()
    val sampleData = (0..6).map { i ->
        GraphDataPoint(
            date = today.minusDays(i.toLong()),
            value = (100..200).random().toFloat(),
        )
    }.reversed()
    val dummyUiState = GlucoseUiState(graphDataPoints = sampleData)
    val scrollState = rememberScrollState()

    MediCareCallTheme {
        GlucoseDetailLayout(
            uiState = dummyUiState,
            selectedTiming = GlucoseTiming.BEFORE_MEAL,
            selectedIndex = sampleData.lastIndex,
            onTimingChange = {},
            onPointClick = {},
            onBack = {},
            scrollState = scrollState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, name = "데이터 없을 때 (Empty View)")
@Composable
fun PreviewGlucoseDetail_Empty() {
    // Empty View 프리뷰
    val dummyUiState = GlucoseUiState(graphDataPoints = emptyList())
    val scrollState = rememberScrollState()

    MediCareCallTheme {
        GlucoseDetailLayout(
            uiState = dummyUiState,
            selectedTiming = GlucoseTiming.AFTER_MEAL,
            selectedIndex = -1,
            onTimingChange = {},
            onPointClick = {},
            onBack = {},
            scrollState = scrollState,
        )
    }
}
