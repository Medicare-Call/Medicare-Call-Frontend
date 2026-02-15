package com.konkuk.medicarecall.ui.feature.statistics.weeklycard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun WeeklyHealthCard(
    modifier: Modifier = Modifier,
    healthNote: String,
) {
    val isUnrecorded = healthNote.isBlank()

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
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "건강징후",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isUnrecorded) "아직 충분한 기록이 쌓이지 않았어요." else healthNote,
                style = MediCareCallTheme.typography.R_16,

                color = if (isUnrecorded) MediCareCallTheme.colors.gray4 else MediCareCallTheme.colors.gray8,
            )
        }
    }
}

@Preview(name = "건강징후 카드 - 기록 있음")
@Composable
fun PreviewWeeklyHealthCardRecorded() {
    WeeklyHealthCard(
        healthNote = "아침·점심 복약과 식사는 문제 없으나, 저녁 약 복용이 늦어질 우려가 있어요. 전반적으로 양호하나 피곤과 호흡곤란을 호소하셨으므로 휴식과 보호자 확인이 필요해요.",
    )
}

@Preview(name = "건강징후 카드 - 미기록")
@Composable
fun PreviewWeeklyHealthCardUnrecorded() {
    WeeklyHealthCard(
        healthNote = "",
    )
}
