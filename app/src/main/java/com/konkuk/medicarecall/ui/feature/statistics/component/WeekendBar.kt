package com.konkuk.medicarecall.ui.feature.statistics.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.LocalDate

@Composable
fun WeekendBar(
    modifier: Modifier = Modifier,
    currentWeek: Pair<LocalDate, LocalDate>,
    isLatestWeek: Boolean,
    isEarliestWeek: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(15.dp, 14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickable(enabled = !isEarliestWeek) { onPreviousWeek() },
                painter = painterResource(id = R.drawable.ic_arrow_big_back),
                contentDescription = "previous week",
                tint = MediCareCallTheme.colors.gray3,
            )

            // 날짜 표시 영역
            WeekRangeLabel(
                startDate = currentWeek.first,
                endDate = currentWeek.second,
                isThisWeek = isLatestWeek,
                modifier = Modifier.weight(1f),
            )

            if (!isLatestWeek) {
                Icon(
                    modifier = Modifier.clickable { onNextWeek() },
                    painter = painterResource(id = R.drawable.ic_arrow_big_forward),
                    contentDescription = "next week",
                    tint = MediCareCallTheme.colors.gray3,
                )
            } else {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MediCareCallTheme.colors.gray2,
            thickness = 1.dp,
        )
    }
}

@Preview(showBackground = true, name = "WeekendBar - 이번주")
@Composable
fun PreviewWeekendBar_ThisWeek() {
    MediCareCallTheme {
        WeekendBar(
            currentWeek = Pair(LocalDate.now(), LocalDate.now()),
            isLatestWeek = true,
            isEarliestWeek = false,
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }
}

@Preview(showBackground = true, name = "WeekendBar - 날짜 범위")
@Composable
fun PreviewWeekendBar_WithDateRange() {
    MediCareCallTheme {
        WeekendBar(
            currentWeek = Pair(LocalDate(2025, 10, 6), LocalDate(2025, 10, 12)),
            isLatestWeek = false,
            isEarliestWeek = false,
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }
}
