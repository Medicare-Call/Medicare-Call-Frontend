package com.konkuk.medicarecall.ui.feature.alarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.alarm.component.AlarmItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.domain.model.type.AlarmType

@Composable
fun AlarmScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "알림",
            leftIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "setting back",
                    modifier = modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = MediCareCallTheme.colors.black,
                )
            },
        )
        AlarmItem(
            AlarmType.NEW_ALARM,
            "✅ 1차 케어콜이 완료되었어요. 확인해 보세요!",
            "7월 8일 13:15",
        )
        AlarmItem(
            AlarmType.READ_ALARM,
            "❗ 박막례 어르신 건강이상 징후가 탐지되었어요. 확인해 주세요!",
            "7월 7일 13:15",
        )
        AlarmItem(
            AlarmType.READ_ALARM,
            "📞 김옥자 어르신 케어콜 부재중 상태입니다. 확인해 주세요!",
            "7월 7일 13:15",
        )
        AlarmItem(
            AlarmType.READ_ALARM,
            "❗ 박막례 어르신 건강이상 징후가 탐지되었어요. 확인해 주세요!",
            "7월 7일 13:15",
        )
        AlarmItem(
            AlarmType.READ_ALARM,
            "✅ 1차 케어콜이 완료되었어요. 확인해 보세요!",
            "7월 7일 13:15",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmScreenPreview() {
    MediCareCallTheme {
        AlarmScreen(
            onBack = {},
        )
    }
}
