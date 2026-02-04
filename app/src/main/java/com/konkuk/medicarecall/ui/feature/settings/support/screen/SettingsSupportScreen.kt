package com.konkuk.medicarecall.ui.feature.settings.support.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun SettingsSupportScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "고객센터",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color.Black,
                )
            },
        )

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_settings_center),
                modifier = modifier.size(80.dp),
                contentDescription = "service_center",
            )
            Spacer(
                modifier = modifier.height(20.dp),
            )
            Text(
                text = "도움이 필요하신가요?",
                style = MediCareCallTheme.typography.R_18,
                color = MediCareCallTheme.colors.gray6,
            )
            Text(
                text = "상담시간 평일 09:00 - 18:00",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray4,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsSupportScreenPreview() {
    MediCareCallTheme {
        SettingsSupportScreen(
            onBack = {},
        )
    }
}
