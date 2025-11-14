package com.konkuk.medicarecall.ui.feature.statistics.weeklycard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.util.WeeklySummaryUtil
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState.WeeklyMealUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun WeeklyMealCard(
    modifier: Modifier = Modifier,
    meal: List<WeeklyMealUiState>,
) {
    Card(
        modifier = modifier
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow01,
                cornerRadius = 14.dp,
            ),

        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            // 1) Title: 식사 통계
            Text(
                "식사통계",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2) 아침+점심+저녁
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                meal.forEach { weeklyMeal ->

                    val isUnrecorded =
                        weeklyMeal.eatenCount == null || weeklyMeal.eatenCount < 0 // -1과 같은 음수 값으로 미기록 판단

                    val iconColor = if (isUnrecorded) {
                        MediCareCallTheme.colors.gray2
                    } else {
                        WeeklySummaryUtil.getMealIconColor(weeklyMeal, MediCareCallTheme.colors)
                    }

                    val countText = if (isUnrecorded) {
                        "- / ${weeklyMeal.totalCount}"
                    } else {
                        "${weeklyMeal.eatenCount}/${weeklyMeal.totalCount}"
                    }

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = weeklyMeal.mealType,
                            style = MediCareCallTheme.typography.R_15,
                            color = MediCareCallTheme.colors.gray8,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            modifier = Modifier
                                .size(16.dp),
                            painter = painterResource(id = R.drawable.ic_filledbowl),
                            contentDescription = null,
                            tint = iconColor,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = countText,
                            style = MediCareCallTheme.typography.R_14,
                            color = if (isUnrecorded) MediCareCallTheme.colors.gray4 else MediCareCallTheme.colors.gray6,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "식사 카드 - 기록 있음")
@Composable
fun PreviewWeeklyMealCardRecorded() {
    WeeklyMealCard(
        modifier = Modifier
            .width(150.dp),
        meal = listOf(
            WeeklyMealUiState("아침", 7, 7),
            WeeklyMealUiState("점심", 5, 7),
            WeeklyMealUiState("저녁", 0, 7),
        ),
    )
}

@Preview(name = "식사 카드 - 미기록")
@Composable
fun PreviewWeeklyMealCardUnrecorded() {
    WeeklyMealCard(
        modifier = Modifier
            .width(150.dp),
        meal = listOf(
            WeeklyMealUiState("아침", -1, 7),
            WeeklyMealUiState("점심", -1, 7),
            WeeklyMealUiState("저녁", -1, 7),
        ),
    )
}
