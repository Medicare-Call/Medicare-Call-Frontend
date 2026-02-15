package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.common.util.GlucoseLevel
import com.konkuk.medicarecall.ui.common.util.classifyGlucose
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlin.math.max

@Composable
fun GlucoseGraph(
    data: List<GraphDataPoint>,
    scrollState: ScrollState,
    selectedIndex: Int,
    onPointClick: (Int) -> Unit,
    timing: GlucoseTiming,
) {
    val colors = MediCareCallTheme.colors
    val lineColor = colors.gray2
    val iconRadiusDp = 4.dp
    val graphDrawingHeightDp = 200.dp
    val labelStyle = MediCareCallTheme.typography.R_14
    val minGlucose = 60f
    val maxGlucose = 200f
    val fixedSectionWidth = 60.dp // 데이터가 많을 때의 최소 섹션 너비

    // [스크롤되는 그래프 영역 | 고정된 Y축 라벨]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediCareCallTheme.colors.white),
        verticalAlignment = Alignment.Top,
    ) {
        // BoxWithConstraints를 사용하여 그래프가 그려질 영역의 실제 너비를 측정
        @SuppressLint("UnusedBoxWithConstraintsScope")
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            // LocalDensity를 사용하여 Dp를 Px로 변환
            val density = LocalDensity.current
            val availableWidthPx = with(density) { maxWidth.toPx() }
            val fixedSectionWidthPx = with(density) { fixedSectionWidth.toPx() }

            val minTotalWidthPx = fixedSectionWidthPx * data.size

            // 그래프의 전체 너비를 Px 단위로 먼저 계산
            val totalGraphWidthPx = max(availableWidthPx, minTotalWidthPx)
            // Px를 다시 Dp로 변환하여 Composable에 적용
            val totalGraphWidth = with(density) { totalGraphWidthPx.toDp() }

            // 최종 섹션 너비도 Dp 단위로 계산
            val sectionWidth: Dp = if (data.isNotEmpty()) totalGraphWidth / data.size else fixedSectionWidth

            Row(
                modifier = Modifier.horizontalScroll(scrollState, reverseScrolling = true),
            ) {
                Column {
                    // 그래프
                    Canvas(
                        modifier = Modifier
                            .width(totalGraphWidth) // 동적으로 계산된 전체 너비 사용
                            .height(graphDrawingHeightDp)
                            .pointerInput(data) {
                                detectTapGestures { tapOffset ->
                                    val sectionWidthPx = sectionWidth.toPx()
                                    val clickedIndex = (tapOffset.x / sectionWidthPx).toInt()
                                    if (clickedIndex in data.indices) {
                                        onPointClick(clickedIndex)
                                    }
                                }
                            },
                    ) {
                        // 혈당 값(value)을 Canvas의 Y좌표로 변환하는 함수
                        fun valueToY(v: Float): Float {
                            val ratio =
                                ((v - minGlucose) / (maxGlucose - minGlucose)).coerceIn(0f, 1f)
                            return size.height * (1f - ratio)
                        }

                        val points = data.mapIndexed { idx, pointData ->
                            // X 좌표 계산 시 동적으로 계산된 섹션 너비 사용
                            val x = (idx * sectionWidth.toPx()) + (sectionWidth.toPx() / 2)
                            val y = valueToY(pointData.value)
                            Offset(x, y)
                        }
                        // 200, 130, 90, 60 가로 가이드라인
                        listOf(200f, 130f, 90f, 60f).forEach { value ->
                            drawLine(
                                color = if (value == 200f || value == 60f) lineColor else lineColor.copy(
                                    alpha = 0.5f,
                                ),
                                start = Offset(0f, valueToY(value)),
                                end = Offset(size.width, valueToY(value)),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = if (value == 130f || value == 90f) PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 10f),
                                ) else null,
                            )
                        }
                        // 선
                        for (i in 0 until points.size - 1) {
                            drawLine(
                                color = lineColor,
                                start = points[i],
                                end = points[i + 1],
                                strokeWidth = 1.5.dp.toPx(),
                            )
                        }
                        // 점
                        points.forEachIndexed { index, point ->
                            val value = data[index].value
                            val color = when (classifyGlucose(value, timing)) { // 공복/식후 기준 반영
                                GlucoseLevel.LOW -> colors.active // 낮음
                                GlucoseLevel.NORMAL -> colors.main // 정상
                                GlucoseLevel.HIGH -> colors.negative // 높음
                            }
                            if (index == selectedIndex) {
                                drawCircle(
                                    color = color.copy(alpha = 0.2f),
                                    radius = iconRadiusDp.toPx() * 3,
                                    center = point,
                                )
                            }
                            drawCircle(color = color, radius = iconRadiusDp.toPx(), center = point)
                        }
                    }
                    // 날짜 라벨
                    Row(modifier = Modifier.width(totalGraphWidth)) { // 동적 너비 사용
                        data.forEach { pointData ->
                            Text(
                                text = "${pointData.date.month.number}.${pointData.date.day}",
                                modifier = Modifier.width(sectionWidth),
                                style = labelStyle,
                                color = colors.gray4,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .width(40.dp)
                .height(graphDrawingHeightDp),
        ) {
            val density = LocalDensity.current
            val heightPx = with(density) { graphDrawingHeightDp.toPx() }

            fun valueToY(v: Float): Float {
                val ratio = ((v - minGlucose) / (maxGlucose - minGlucose)).coerceIn(0f, 1f)
                return heightPx * (1f - ratio)
            }

            listOf(200, 130, 90, 60).forEach { value ->
                val yPosPx = valueToY(value.toFloat())
                val yPosDp = with(density) { yPosPx.toDp() }

                Text(
                    text = value.toString(),
                    style = labelStyle.copy(fontSize = 12.sp),
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 8.dp, y = yPosDp - 7.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "데이터 2개일 때")
@Composable
fun PreviewGlucoseGraph_TwoPoints() {
    val today = LocalDate.now()
    val sampleData = (0..1).map {
        GraphDataPoint(
            date = today.minus(it, DateTimeUnit.DAY),
            value = (70..210).random().toFloat(),
        )
    }.reversed()
    val scrollState = rememberScrollState()

    MediCareCallTheme {
        GlucoseGraph(
            data = sampleData,
            selectedIndex = sampleData.lastIndex,
            onPointClick = {},
            scrollState = scrollState,
            timing = GlucoseTiming.BEFORE_MEAL,
        )
    }
}

@Preview(showBackground = true, name = "데이터 14개일 때")
@Composable
fun PreviewGlucoseGraph_ManyPoints() {
    val scrollState = rememberScrollState()
    val today = LocalDate.now()
    // 14일치 가상 데이터
    val sampleData = (0..13).map {
        GraphDataPoint(
            date = today.minus(it, DateTimeUnit.DAY),
            value = (70..210).random().toFloat(),
        )
    }.reversed()
    MediCareCallTheme {
        GlucoseGraph(
            data = sampleData,
            selectedIndex = sampleData.lastIndex,
            onPointClick = {},
            scrollState = scrollState,
            timing = GlucoseTiming.AFTER_MEAL,
        )
    }
}
