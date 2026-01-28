package com.konkuk.medicarecall.ui.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.PushNotificationDto
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.component.SwitchButton
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailMyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingAlarmScreen(
    modifier: Modifier = Modifier,
    myDataViewModel: DetailMyDataViewModel = koinViewModel(),
    myDataInfo: MyInfoResponseDto,
    onBack: () -> Unit = {},
) {
    // ViewModel 상태 구독
    val masterChecked by myDataViewModel.masterChecked.collectAsStateWithLifecycle()
    val completeChecked by myDataViewModel.completeChecked.collectAsStateWithLifecycle()
    val abnormalChecked by myDataViewModel.abnormalChecked.collectAsStateWithLifecycle()
    val missedChecked by myDataViewModel.missedChecked.collectAsStateWithLifecycle()

    // 초기 데이터 로드
    LaunchedEffect(myDataInfo) {
        myDataViewModel.initializeNotificationSettings(myDataInfo)
    }

    // 상태를 업데이트하고 ViewModel을 호출하는 함수를 만듭니다. (코드 중복 제거)
    val updateSettings = {
        myDataViewModel.updateUserData(
            userInfo = myDataInfo.copy(
                // 기존 데이터를 복사하여 변경사항만 적용
                pushNotification = PushNotificationDto(
                    all = if (masterChecked) "ON" else "OFF",
                    carecallCompleted = if (completeChecked) "ON" else "OFF",
                    healthAlert = if (abnormalChecked) "ON" else "OFF",
                    carecallMissed = if (missedChecked) "ON" else "OFF",
                ),
            ),
        )
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
                    checked = masterChecked,
                    onCheckedChange = { isChecked ->
                        myDataViewModel.setMasterChecked(isChecked)
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
                    checked = completeChecked,
                    onCheckedChange = { isChecked ->
                        myDataViewModel.setCompleteChecked(isChecked)
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
                    checked = abnormalChecked,
                    onCheckedChange = { isChecked ->
                        myDataViewModel.setAbnormalChecked(isChecked)
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
                    checked = missedChecked,
                    onCheckedChange = { isChecked ->
                        myDataViewModel.setMissedChecked(isChecked)
                        updateSettings()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingAlarmScreenPreview() {
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
