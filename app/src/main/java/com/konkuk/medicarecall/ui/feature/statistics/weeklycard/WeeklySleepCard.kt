package com.konkuk.medicarecall.ui.feature.statistics.weeklycard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun WeeklySleepCard(
    modifier: Modifier = Modifier,
    summary: WeeklySummaryUiState,
) {
    val isUnrecorded = summary.weeklySleepHours == null
    val hoursText = if (isUnrecorded) "-" else summary.weeklySleepHours.toString()
    val minutesText = if (isUnrecorded) "-" else summary.weeklySleepMinutes.toString()
    val textColor = if (isUnrecorded) MediCareCallTheme.colors.gray4 else MediCareCallTheme.colors.gray8

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
            // 1) Title: 평균 수면
            Text(
                "평균수면",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 2) 아이콘
            Icon(
                painter = painterResource(id = R.drawable.ic_moon),
                contentDescription = "moon",
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 3) 시간

            Row(
                modifier = Modifier.padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = hoursText,
                    style = MediCareCallTheme.typography.SB_22,
                    color = textColor,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.offset(y = (2).dp),
                    text = "시간",
                    style = MediCareCallTheme.typography.M_16,
                    color = MediCareCallTheme.colors.gray8,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = minutesText,
                    style = MediCareCallTheme.typography.SB_22,
                    color = textColor,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.offset(y = (2).dp),
                    text = "분",
                    style = MediCareCallTheme.typography.M_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }
        }
    }
}

@Preview(name = "수면 카드 - 기록 있음")
@Composable
fun PreviewWeeklySleepCardRecorded() {
    WeeklySleepCard(
        modifier = Modifier.width(155.dp),
        summary = WeeklySummaryUiState(
            weeklySleepHours = 7,
            weeklySleepMinutes = 12,
            weeklyMealRate = 0,
            weeklyMedicineRate = 0,
            weeklyHealthIssueCount = 0,
            weeklyUnansweredCount = 0,
        ),
    )
}

@Preview(name = "수면 카드 - 미기록")
@Composable
fun PreviewWeeklySleepCardUnrecorded() {
    WeeklySleepCard(
        modifier = Modifier.width(155.dp),
        summary = WeeklySummaryUiState(),
    )
}
