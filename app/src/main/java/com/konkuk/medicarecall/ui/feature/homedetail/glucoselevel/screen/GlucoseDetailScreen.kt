package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.component.TopAppBar
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlucoseDetailScreen(
    modifier: Modifier = Modifier,
    elderId: Long,
    onBack: () -> Unit,
    viewModel: GlucoseViewModel = koinViewModel(),
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 페이지 카운터
    val counter = remember {
        mutableStateMapOf(
            GlucoseTiming.BEFORE_MEAL to 0,
            GlucoseTiming.AFTER_MEAL to 0,
        )
    }

    // 로딩 요청 중복 방지를 위한 플래그
    val isRequestingMore = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // 데이터 새로고침 로직
    val refreshData = remember(viewModel) {
        {
            counter[GlucoseTiming.BEFORE_MEAL] = 0
            counter[GlucoseTiming.AFTER_MEAL] = 0

            viewModel.getGlucoseData(
                elderId = elderId,
                counter = 0,
                type = GlucoseTiming.BEFORE_MEAL,
                isRefresh = true,
            )
            viewModel.getGlucoseData(
                elderId = elderId,
                counter = 0,
                type = GlucoseTiming.AFTER_MEAL,
                isRefresh = true,
            )
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
        // 스크롤이 거의 끝까지 왔을 때만 다음 페이지 불러오기
        val shouldLoad = scrollState.value > scrollState.maxValue - 500 || scrollState.maxValue <= 100

        if (shouldLoad &&
            !uiState.isLoading &&
            uiState.hasNext &&
            !isRequestingMore.value
        ) {
            isRequestingMore.value = true
            val currentTiming = uiState.selectedTiming
            val currentPage = counter.getValue(currentTiming)

            viewModel.getGlucoseData(
                elderId = elderId,
                counter = currentPage + 1, // 다음 페이지 요청
                type = currentTiming,
                isRefresh = false,
            )

            counter[currentTiming] = currentPage + 1
        }
    }

    // 로딩 완료 시 플래그 리셋
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRequestingMore.value = false
        }
    }

    GlucoseDetailScreenLayout(
        modifier = modifier,
        uiState = uiState,
        selectedTiming = uiState.selectedTiming,
        selectedIndex = uiState.selectedIndex,

        // '공복'/'식후' 버튼
        onTimingChange = { newTiming ->
            viewModel.updateTiming(newTiming)
            coroutineScope.launch { scrollState.scrollTo(0) }
        },
        // 그래프 점
        onPointClick = viewModel::onClickDots,
        scrollState = scrollState,
        onBack = onBack,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GlucoseDetailScreenLayout(
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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(186.dp))
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
                Spacer(modifier = Modifier.height(248.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, name = "데이터 있을 때")
@Composable
fun PreviewGlucoseDetailScreenDataAvailable() {
    // 더미 데이터 프리뷰
    val today = LocalDate.now()
    val sampleData = (0..6).map { i ->
        GraphDataPoint(
            date = today.minus(i, DateTimeUnit.DAY),
            value = (100..200).random().toFloat(),
        )
    }.reversed()
    val dummyUiState = GlucoseUiState(graphDataPoints = sampleData)
    val scrollState = rememberScrollState()

    MediCareCallTheme {
        GlucoseDetailScreenLayout(
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
fun PreviewGlucoseDetailScreenEmpty() {
    // Empty View 프리뷰
    val dummyUiState = GlucoseUiState(graphDataPoints = emptyList())
    val scrollState = rememberScrollState()

    MediCareCallTheme {
        GlucoseDetailScreenLayout(
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
