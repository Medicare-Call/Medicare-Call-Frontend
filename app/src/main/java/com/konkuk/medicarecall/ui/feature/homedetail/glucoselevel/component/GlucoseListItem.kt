package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun GlucoseListItem(
    modifier: Modifier = Modifier,
    date: LocalDate,
    timingLabel: String,
    value: Int,
    timing: GlucoseTiming,
) {
    val formatter = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN)
    val formattedDate = date.format(formatter)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
        ) {
            // 날짜 텍스트
            Text(
                text = formattedDate,
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray6,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    // 아침|공복 or 저녁|식후
                    text = timingLabel,
                    style = MediCareCallTheme.typography.R_14,
                    color = MediCareCallTheme.colors.gray6,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        // 혈당값
                        text = value.toString(),
                        style = MediCareCallTheme.typography.SB_16,
                        color = MediCareCallTheme.colors.gray6,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "mg/dL",
                        style = MediCareCallTheme.typography.R_14,
                        color = MediCareCallTheme.colors.gray6,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            GlucoseStatusChip(
                value = value,
                timing = timing,
            ) // 낮음,정상,높음
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGlucoseListItem() {
    MediCareCallTheme {
        GlucoseListItem(
            date = LocalDate.now(),
            timingLabel = "아침 | 공복",
            value = 180,
            timing = GlucoseTiming.BEFORE_MEAL,
        )
    }
}
