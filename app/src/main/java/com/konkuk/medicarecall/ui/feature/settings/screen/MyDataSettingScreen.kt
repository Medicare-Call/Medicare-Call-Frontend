package com.konkuk.medicarecall.ui.feature.settings.screen

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.konkuk.medicarecall.MainActivity
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.LogoutConfirmDialog
import com.konkuk.medicarecall.ui.feature.settings.component.SettingInfoItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.MyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import com.konkuk.medicarecall.ui.type.GenderType

@Composable
fun MyDataSettingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToUserInfoSetting: (myInfo: MyInfoResponseDto) -> Unit = {},
    navigateToLoginAfterLogout: () -> Unit = {},
    myDataViewModel: MyDataViewModel = hiltViewModel(),
) {
    val myDataInfo = myDataViewModel.myDataInfo
    var showLogoutDialog by remember { mutableStateOf(false) }
    val gender = when (myDataInfo?.gender) {
        GenderType.FEMALE -> "여성"
        else -> "남성"
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                myDataViewModel.refresh() // 복귀 시 재조회
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "내 정보 설정",
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
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp,
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "내 정보",
                        style = MediCareCallTheme.typography.SB_18,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        text = "편집",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.active,
                        modifier = modifier.clickable(
                            onClick = {
                                // 네비게이션을 통해 MyDetail 화면으로 이동
                                navigateToUserInfoSetting(myDataInfo)
                            },
                        ),
                    )
                }
                SettingInfoItem("이름", myDataInfo?.name ?: "이름 없음")
                SettingInfoItem("생일", formatDateToKorean((myDataInfo?.birthDate ?: "날짜 정보가 없습니다")))
                SettingInfoItem("성별", gender)
                SettingInfoItem("휴대폰번호", formatPhoneNumber(myDataInfo?.phone ?: "전화번호 정보가 없습니다"))
            }

            Spacer(modifier = modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp,
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    "계정 관리",
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8,
                )
                Text(
                    text = "로그아웃",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showLogoutDialog = true
                            navigateToLoginAfterLogout()
                        },
                )

                Text(
                    text = "서비스 탈퇴",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onDismiss = { showLogoutDialog = false },
            onLogout = {
                myDataViewModel.logout(
                    onSuccess = {
                        Log.d("MyDataSettingScreen", "Logout successful")
                        // 로그아웃 성공 후 동작
                        showLogoutDialog = false
                        val intent = Intent(context, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                        context.startActivity(intent)
                    },
                    onError = { error ->
                        // 로그아웃 실패 처리 (예: 에러 메시지 표시)
                        Log.e("MyDataSettingScreen", "Logout failed: $error")
                    },
                )
            },
        )
    }
}

fun formatPhoneNumber(number: String): String {
    return number.replaceFirst(
        "(\\d{3})(\\d{4})(\\d{4})".toRegex(),
        "$1-$2-$3",
    )
}
