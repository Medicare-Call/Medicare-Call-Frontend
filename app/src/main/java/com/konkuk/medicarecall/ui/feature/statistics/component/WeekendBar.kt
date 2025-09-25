package com.konkuk.medicarecall.ui.feature.statistics.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeekendBar(
    modifier: Modifier = Modifier,
    currentWeek: Pair<LocalDate, LocalDate>,
    isLatestWeek: Boolean,
    isEarliestWeek: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // 왼쪽 화살표 (이전 주로 이동)
        Icon(
            modifier = Modifier.clickable { onPreviousWeek() },
            painter = painterResource(id = R.drawable.ic_arrow_big_back),
            contentDescription = "previous week",
            tint = MediCareCallTheme.colors.gray3
        )

        // 날짜 텍스트 ('이번주' 또는 날짜 범위 표시)
        Text(
            modifier = Modifier.weight(1f),
            text = if (isLatestWeek) {
                "이번주"
            } else {
                val formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
                "${currentWeek.first.format(formatter)} - ${currentWeek.second.format(formatter)}"
            },
            style = MediCareCallTheme.typography.M_20,
            color = Color(0xFF444444),
            textAlign = TextAlign.Center
        )

        // 오른쪽 화살표 ('이번주'가 아닐 때만 표시)
        if (!isLatestWeek) {
            Icon(
                modifier = Modifier.clickable { onNextWeek() },
                painter = painterResource(id = R.drawable.ic_arrow_big_forward),
                contentDescription = "next week",
                tint = MediCareCallTheme.colors.gray3
            )
        } else {

            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWeekendBar() {
    WeekendBar(
        currentWeek = Pair(LocalDate.now(), LocalDate.now()),
        isLatestWeek = true,
        isEarliestWeek = true,
        onPreviousWeek = {},
        onNextWeek = {}
    )
}
