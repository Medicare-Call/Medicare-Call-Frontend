package com.konkuk.medicarecall.ui.feature.settings.notification.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.domain.model.PushNotification
import com.konkuk.medicarecall.ui.feature.settings.notification.component.SwitchButton
import com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel.SettingsEditMyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsNotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsEditMyDataViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMyInfo()
    }

    LaunchedEffect(uiState.myDataInfo) {
        uiState.myDataInfo?.let { viewModel.initializeNotificationSettings(it) }
    }

    val updateSettings = {
        uiState.myDataInfo?.let { info ->
            viewModel.updateUserData(
                userInfo = info.copy(
                    pushNotification = PushNotification(
                        all = if (uiState.masterChecked) "ON" else "OFF",
                        carecallCompleted = if (uiState.completeChecked) "ON" else "OFF",
                        healthAlert = if (uiState.abnormalChecked) "ON" else "OFF",
                        carecallMissed = if (uiState.missedChecked) "ON" else "OFF",
                    ),
                ),
            )
        }
    }

    if (uiState.isLoading && uiState.myDataInfo == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "푸시 알림 설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color.Black,
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // 전체 푸시 알림
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("전체 푸시 알림", style = MediCareCallTheme.typography.SB_16, color = Color.Black)
                SwitchButton(
                    checked = uiState.masterChecked,
                    onCheckedChange = { isChecked ->
                        viewModel.setMasterChecked(isChecked)
                        updateSettings()
                    },
                )
            }
            // 케어콜 완료 알림
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "케어콜 완료 알림",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
                SwitchButton(
                    checked = uiState.completeChecked,
                    onCheckedChange = { isChecked ->
                        viewModel.setCompleteChecked(isChecked)
                        updateSettings()
                    },
                )
            }
            // 건강 이상 징후 알림
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "건강 이상 징후 알림",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
                SwitchButton(
                    checked = uiState.abnormalChecked,
                    onCheckedChange = { isChecked ->
                        viewModel.setAbnormalChecked(isChecked)
                        updateSettings()
                    },
                )
            }
            // 케어콜 부재중 알림
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "케어콜 부재중 알림",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
                SwitchButton(
                    checked = uiState.missedChecked,
                    onCheckedChange = { isChecked ->
                        viewModel.setMissedChecked(isChecked)
                        updateSettings()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsNotificationScreenPreview() {
    MediCareCallTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg)
                .statusBarsPadding(),
        ) {
            SettingsTopAppBar(
                title = "푸시 알림 설정",
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_back),
                        contentDescription = "go_back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                    )
                },
            )
        }
    }
}
