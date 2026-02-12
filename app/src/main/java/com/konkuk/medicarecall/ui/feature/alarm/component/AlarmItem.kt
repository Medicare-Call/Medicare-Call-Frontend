package com.konkuk.medicarecall.ui.feature.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.domain.model.type.AlarmType

@Composable
fun AlarmItem(
    alarmType: AlarmType,
    content: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(if (alarmType == AlarmType.NEW_ALARM) MediCareCallTheme.colors.g50 else Color.Transparent)
            .padding(14.dp),
    ) {
        Text(
            text = content,
            color = MediCareCallTheme.colors.black,
            style = MediCareCallTheme.typography.R_15,
        )
        Text(
            text = date,
            color = MediCareCallTheme.colors.gray5,
            style = MediCareCallTheme.typography.R_14,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmItemPreview() {
    Column {
        AlarmItem(
            AlarmType.NEW_ALARM,
            "✅ 1차 케어콜이 완료되었어요. 확인해 보세요!",
            "7월 8일 13:15",
        )
        AlarmItem(
            AlarmType.READ_ALARM,
            "❗ 박막례 어스신 건강이상 징후가 탐지되었어요. 확인해 주세요!",
            "7월 7일 13:15",
        )
    }
}
