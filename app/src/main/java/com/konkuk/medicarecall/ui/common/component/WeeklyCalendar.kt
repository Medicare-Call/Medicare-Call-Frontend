package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate

@Composable
fun WeeklyCalendar(
    selectedDate: LocalDate,
    weekDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit, // 날짜 클릭 시 동작할 콜백
) {
    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토") // 요일 표시

    Column(modifier = Modifier.Companion.fillMaxWidth()) {
        // 요일 (일~토)
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            weekDays.forEach { day ->
                Box(
                    modifier = Modifier.Companion,
                    contentAlignment = Alignment.Companion.Center,
                ) {
                    Text(
                        modifier = Modifier.Companion
                            .padding(horizontal = 6.5.dp),
                        text = day,
                        style = MediCareCallTheme.typography.R_18,
                        color = MediCareCallTheme.colors.gray4,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.Companion.height(3.dp))
        // 해당 주 날짜
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            weekDates.forEach { date ->

                val isSelected = date == selectedDate

                Box(
                    modifier = Modifier.Companion
                        .size(29.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MediCareCallTheme.colors.main else Color.Companion.Transparent)
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Companion.Center,
                ) {
                    Text(
                        modifier = Modifier.Companion,
                        text = "${date.dayOfMonth}",
                        style = if (isSelected) MediCareCallTheme.typography.SB_18 else MediCareCallTheme.typography.M_17,
                        color = if (isSelected) Color.Companion.White else MediCareCallTheme.colors.gray4,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeeklyCalendar() {
    val baseDate = LocalDate.of(2025, 5, 5)
    val week = (0..6).map { baseDate.plusDays(it.toLong()) }

    WeeklyCalendar(
        weekDates = week,
        selectedDate = baseDate.plusDays(2),
        onDateSelected = {},
    )
}
