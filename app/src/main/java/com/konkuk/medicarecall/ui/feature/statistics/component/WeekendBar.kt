import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    onNextWeek: () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(15.dp, 14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.clickable(enabled = !isEarliestWeek) { onPreviousWeek() },
                painter = painterResource(id = R.drawable.ic_arrow_big_back),
                contentDescription = "previous week",
                tint = MediCareCallTheme.colors.gray3,
            )

            // 날짜 표시 영역
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isLatestWeek) {
                    Text(
                        text = "이번주",
                        style = MediCareCallTheme.typography.M_20,
                        color = MediCareCallTheme.colors.gray8,
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
                        val commonTextStyle = MediCareCallTheme.typography.M_20
                        val commonTextColor = MediCareCallTheme.colors.gray8

                        Text(text = currentWeek.first.format(formatter), style = commonTextStyle, color = commonTextColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "-", style = commonTextStyle, color = commonTextColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = currentWeek.second.format(formatter), style = commonTextStyle, color = commonTextColor)
                    }
                }
            }


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
@Preview(showBackground = true)
@Composable
fun PreviewWeekendBar() {
    WeekendBar(
        currentWeek = Pair(LocalDate.now(), LocalDate.now()),
        isLatestWeek = true,
        isEarliestWeek = true,
        onPreviousWeek = {},
        onNextWeek = {},
    )
}

@Preview(name = "날짜 범위 표시 (월-일)", showBackground = true)
@Composable
fun PreviewWeekendBar_WithDateRange() {

    val monday = LocalDate.of(2025, 10, 6)
    val sunday = LocalDate.of(2025, 10, 12)

    WeekendBar(
        currentWeek = Pair(monday, sunday),
        isLatestWeek = false,
        isEarliestWeek = false,
        onPreviousWeek = {},
        onNextWeek = {},
    )
}
