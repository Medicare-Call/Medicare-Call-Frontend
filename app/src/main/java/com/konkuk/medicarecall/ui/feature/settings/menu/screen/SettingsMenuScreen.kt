package com.konkuk.medicarecall.ui.feature.settings.menu.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.menu.viewmodel.SettingsMenuViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsMenuScreen(
    navigateToUserInfo: () -> Unit = {},
    navigateToNotice: () -> Unit = {},
    navigateToCenter: () -> Unit = {},
    navigateToSubscribe: () -> Unit = {},
    navigateToElderPersonalInfo: () -> Unit = {},
    navigateToElderHealthInfo: () -> Unit = {},
    navigateToNotificationSetting: () -> Unit = {},
    settingsMenuViewModel: SettingsMenuViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                settingsMenuViewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val uiState by settingsMenuViewModel.uiState.collectAsStateWithLifecycle()

    SettingsMenuLayout(
        userName = uiState.userInfo.name,
        onProfileClick = navigateToUserInfo,
        onNoticeClick = navigateToNotice,
        onCenterClick = navigateToCenter,
        onSubscribeClick = navigateToSubscribe,
        onPaymentDetailClick = {},
        onElderPersonalInfoClick = navigateToElderPersonalInfo,
        onElderHealthInfoClick = navigateToElderHealthInfo,
        onCareCallScheduleClick = {},
        onNotificationSettingClick = navigateToNotificationSetting,
    )
}

@Composable
private fun SettingsMenuLayout(
    userName: String,
    onProfileClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onCenterClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    onPaymentDetailClick: () -> Unit,
    onElderPersonalInfoClick: () -> Unit,
    onElderHealthInfoClick: () -> Unit,
    onCareCallScheduleClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(title = "설정")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            // 프로필
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick() }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_setting_profile),
                    contentDescription = "settings profile image",
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = userName,
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "님",
                    style = MediCareCallTheme.typography.R_18,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_big),
                    contentDescription = "화살표 아이콘",
                    modifier = Modifier.size(28.dp),
                    tint = MediCareCallTheme.colors.gray2,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .figmaShadow(
                            group = MediCareCallTheme.shadow.shadow01,
                            cornerRadius = 14.dp,
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCareCallTheme.colors.white)
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onNoticeClick() }
                            .weight(2f),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_announcement),
                            contentDescription = "공지사항 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "공지사항",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clickable { onCenterClick() }
                            .weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_service_center),
                            contentDescription = "고객센터 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "고객센터",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clickable { onSubscribeClick() }
                            .weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_subscription_management),
                            contentDescription = "구독관리 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "구독관리",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clickable { onPaymentDetailClick() }
                            .weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_payment_detail),
                            contentDescription = "결제내역 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "결제내역",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .figmaShadow(
                            group = MediCareCallTheme.shadow.shadow01,
                            cornerRadius = 14.dp,
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCareCallTheme.colors.white)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onElderPersonalInfoClick),
                    ) {
                        Text(
                            text = "어르신 개인정보 설정",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "화살표 아이콘",
                            modifier = Modifier.size(24.dp),
                            tint = MediCareCallTheme.colors.gray2,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onElderHealthInfoClick),
                    ) {
                        Text(
                            text = "어르신 건강정보 설정",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "화살표 아이콘",
                            modifier = Modifier.size(24.dp),
                            tint = MediCareCallTheme.colors.gray2,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onCareCallScheduleClick),
                    ) {
                        Text(
                            text = "케어콜 스케줄 설정",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "화살표 아이콘",
                            modifier = Modifier.size(24.dp),
                            tint = MediCareCallTheme.colors.gray2,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNotificationSettingClick),
                    ) {
                        Text(
                            text = "푸시 알림 설정",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "화살표 아이콘",
                            modifier = Modifier.size(24.dp),
                            tint = MediCareCallTheme.colors.gray2,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsMenuLayoutPreview() {
    MediCareCallTheme {
        SettingsMenuLayout(
            userName = "홍길동",
            onProfileClick = {},
            onNoticeClick = {},
            onCenterClick = {},
            onSubscribeClick = {},
            onPaymentDetailClick = {},
            onElderPersonalInfoClick = {},
            onElderHealthInfoClick = {},
            onCareCallScheduleClick = {},
            onNotificationSettingClick = {},
        )
    }
}
