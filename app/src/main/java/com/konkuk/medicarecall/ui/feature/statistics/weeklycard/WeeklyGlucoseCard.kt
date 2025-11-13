package com.konkuk.medicarecall.ui.feature.statistics.weeklycard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun WeeklyGlucoseCard(
    modifier: Modifier = Modifier,
    weeklyGlucose: WeeklySummaryUiState.WeeklyGlucoseUiState,
) {
    val hasBeforeMealData =
        weeklyGlucose.beforeMealNormal > 0 || weeklyGlucose.beforeMealHigh > 0 || weeklyGlucose.beforeMealLow > 0
    val hasAfterMealData =
        weeklyGlucose.afterMealNormal > 0 || weeklyGlucose.afterMealHigh > 0 || weeklyGlucose.afterMealLow > 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow01,
                cornerRadius = 14.dp,
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {
            Text(
                text = "혈당",
                style = MediCareCallTheme.typography.R_16,
                color = MediCareCallTheme.colors.gray4,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top,
            ) {
                // --- 공복 혈당 ---
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "공복",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center,
                    ) {
                        if (hasBeforeMealData) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (weeklyGlucose.beforeMealNormal > 0) GlucoseStatusRow(
                                    "정상",
                                    weeklyGlucose.beforeMealNormal,
                                )
                                if (weeklyGlucose.beforeMealHigh > 0) GlucoseStatusRow(
                                    "높음",
                                    weeklyGlucose.beforeMealHigh,
                                )
                                if (weeklyGlucose.beforeMealLow > 0) GlucoseStatusRow(
                                    "낮음",
                                    weeklyGlucose.beforeMealLow,
                                )
                            }
                        } else {
                            Text(
                                text = "아직 충분한 기록이\n쌓이지 않았어요.",
                                style = MediCareCallTheme.typography.R_14,
                                color = MediCareCallTheme.colors.gray4,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = MediCareCallTheme.colors.gray1,
                    )
                }

                // --- 식후 혈당 ---
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "식후",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center,
                    ) {
                        if (hasAfterMealData) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (weeklyGlucose.afterMealNormal > 0) GlucoseStatusRow(
                                    "정상",
                                    weeklyGlucose.afterMealNormal,
                                )
                                if (weeklyGlucose.afterMealHigh > 0) GlucoseStatusRow(
                                    "높음",
                                    weeklyGlucose.afterMealHigh,
                                )
                                if (weeklyGlucose.afterMealLow > 0) GlucoseStatusRow(
                                    "낮음",
                                    weeklyGlucose.afterMealLow,
                                )
                            }
                        } else {
                            Text(
                                text = "아직 충분한 기록이\n쌓이지 않았어요.",
                                style = MediCareCallTheme.typography.R_14,
                                color = MediCareCallTheme.colors.gray4,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlucoseStatusRow(
    status: String,
    count: Int,
) {
    if (count == 0) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.component.WeeklyGlucoseStatusChip(
            statusText = status,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${count}번",
            style = MediCareCallTheme.typography.R_14,
            color = MediCareCallTheme.colors.gray6,
        )
    }
}

@Preview(name = "혈당 카드 - 기록 있음")
@Composable
fun PreviewWeeklyGlucoseCardRecorded() {
    WeeklyGlucoseCard(
        weeklyGlucose = WeeklySummaryUiState.WeeklyGlucoseUiState(
            beforeMealNormal = 5,
            beforeMealHigh = 2,
            beforeMealLow = 1,
            afterMealNormal = 5,
            afterMealLow = 2,
            afterMealHigh = 0,
        ),
    )
}

@Preview(name = "혈당 카드 - 미기록")
@Composable
fun PreviewWeeklyGlucoseCardUnrecorded() {
    WeeklyGlucoseCard(
        weeklyGlucose = WeeklySummaryUiState.WeeklyGlucoseUiState.EMPTY,
    )
}
