package com.konkuk.medicarecall.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import com.konkuk.medicarecall.ui.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun ServiceCenterScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "고객센터",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        )
        Spacer(modifier = modifier.height(30.dp))
        Column {
            Text(
                text = "도움이 필요하신가요?",
                style = MediCareCallTheme.typography.SB_16,
                color = Color.Black
            )
            Spacer(modifier = modifier.height(7.dp))
            Text(
                text = "상담시간 평일 09:00 - 18:00",
                style = MediCareCallTheme.typography.R_14,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun ServiceCenterPreview() {
    ServiceCenterScreen()
}