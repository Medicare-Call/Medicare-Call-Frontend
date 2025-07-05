package com.konkuk.medicarecall.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.settings.component.SettingTextField
import com.konkuk.medicarecall.ui.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun HealthDetailScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().background(MediCareCallTheme.colors.bg)) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "어르신 건강정보 설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        )
        Column(modifier = modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingTextField("질환 정보","당뇨","질환명")
            // 복약정보
            // 특이사항

            Button(
                modifier = modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = MediCareCallTheme.colors.white,
                    containerColor = MediCareCallTheme.colors.main
                )

            ) {
                Text("확인")
            }
        }
    }
}