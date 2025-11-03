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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.PushNotificationDto
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.component.SwitchButton
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailMyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun SettingAlarmScreen(
    modifier: Modifier = Modifier,
    myDataViewModel: DetailMyDataViewModel = hiltViewModel(),
    myDataInfo: MyInfoResponseDto,
    onBack: () -> Unit = {},
) {
    // 1. UI를 위한 로컬 상태를 선언합니다.
    var masterChecked by remember { mutableStateOf(false) }
    var completeChecked by remember { mutableStateOf(false) }
    var abnormalChecked by remember { mutableStateOf(false) }
    var missedChecked by remember { mutableStateOf(false) }

    // 2. `LaunchedEffect`를 사용해 외부 데이터(myDataInfo)가 바뀔 때마다 로컬 상태를 동기화합니다.
    //    이렇게 하면 데이터가 변경되었을 때 UI가 즉시 올바르게 반영됩니다.
    LaunchedEffect(myDataInfo) {
        masterChecked = myDataInfo.pushNotification.all == "ON"
        completeChecked = myDataInfo.pushNotification.carecallCompleted == "ON" || masterChecked
        abnormalChecked = myDataInfo.pushNotification.healthAlert == "ON" || masterChecked
        missedChecked = myDataInfo.pushNotification.carecallMissed == "ON" || masterChecked
    }

    // 3. 상태를 업데이트하고 ViewModel을 호출하는 함수를 만듭니다. (코드 중복 제거)
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
                        // 4. 상태를 먼저 모두 변경하고,
                        masterChecked = isChecked
                        completeChecked = isChecked
                        abnormalChecked = isChecked
                        missedChecked = isChecked
                        // 5. 마지막에 변경된 최종 상태로 ViewModel을 호출합니다.
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
                        completeChecked = isChecked
                        if (!isChecked) {
                            masterChecked = false
                        }
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
                        abnormalChecked = isChecked
                        if (!isChecked) {
                            masterChecked = false
                        }
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
                        missedChecked = isChecked
                        if (!isChecked) {
                            masterChecked = false
                        }
                        updateSettings()
                    },
                )
            }
        }
    }
}
