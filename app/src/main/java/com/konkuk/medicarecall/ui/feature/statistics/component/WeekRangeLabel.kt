package com.konkuk.medicarecall.ui.feature.statistics.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun WeekRangeLabel(
    startDate: LocalDate,
    endDate: LocalDate,
    isThisWeek: Boolean,
    modifier: Modifier = Modifier,
) {
    val formatter = remember {
        LocalDate.Format {
            monthNumber()
            char('월')
            char(' ')
            day()
            char('일')
        }
    }
    val textStyle = MediCareCallTheme.typography.M_20
    val textColor = MediCareCallTheme.colors.gray8

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (isThisWeek) {
            Text(text = "이번주", style = textStyle, color = textColor)
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 시작 날짜 (예: 월요일)
                Text(startDate.format(formatter), style = textStyle, color = textColor)
                Spacer(Modifier.width(8.dp))
                Text("-", style = textStyle, color = textColor)
                Spacer(Modifier.width(8.dp))
                // 종료 날짜 (예: 일요일)
                Text(endDate.format(formatter), style = textStyle, color = textColor)
            }
        }
    }
}

@Preview(showBackground = true, name = "이번주 표시")
@Composable
fun PreviewWeekRangeLabel_ThisWeek() {
    MediCareCallTheme {
        WeekRangeLabel(
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            isThisWeek = true,
        )
    }
}

@Preview(showBackground = true, name = "날짜 범위 표시 (10월 6일 ~ 10월 12일)")
@Composable
fun PreviewWeekRangeLabel_WithRange() {
    MediCareCallTheme {
        WeekRangeLabel(
            startDate = LocalDate(2025, 10, 6),
            endDate = LocalDate(2025, 10, 12),
            isThisWeek = false,
        )
    }
}
