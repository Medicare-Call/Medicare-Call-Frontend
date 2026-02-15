package com.konkuk.medicarecall.ui.feature.settings.mydata.screen

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.MainActivity
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.util.formatDateToKorean
import com.konkuk.medicarecall.ui.feature.settings.mydata.component.LogoutConfirmDialog
import com.konkuk.medicarecall.ui.feature.settings.component.SettingInfoItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel.SettingsMyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import com.konkuk.medicarecall.domain.model.type.GenderType
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsMyDataScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToUserInfoSetting: () -> Unit = {},
    navigateToLoginAfterLogout: () -> Unit = {},
    viewModel: SettingsMyDataViewModel = koinViewModel(),
) {
    val myDataInfo by viewModel.myDataInfo.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    SettingsMyDataLayout(
        modifier = modifier,
        name = myDataInfo.name,
        birthDate = myDataInfo.birthDate,
        gender = myDataInfo.gender,
        phone = myDataInfo.phoneNumber,
        showLogoutDialog = showLogoutDialog,
        onBackClick = onBack,
        onEditClick = navigateToUserInfoSetting,
        onLogoutClick = { showLogoutDialog = true },
        onServiceWithdrawClick = {},
        onLogoutDismiss = { showLogoutDialog = false },
        onLogoutConfirm = {
            viewModel.logout(
                onSuccess = {
                    showLogoutDialog = false
                    val intent = Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    context.startActivity(intent)
                    navigateToLoginAfterLogout()
                },
                onError = { error ->
                },
            )
        },
    )
}

@Composable
private fun SettingsMyDataLayout(
    modifier: Modifier = Modifier,
    name: String,
    birthDate: String,
    gender: GenderType,
    phone: String,
    showLogoutDialog: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onServiceWithdrawClick: () -> Unit,
    onLogoutDismiss: () -> Unit,
    onLogoutConfirm: () -> Unit,
) {
    val genderText = when (gender) {
        GenderType.FEMALE -> "여성"
        else -> "남성"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "내 정보 설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() },
                    tint = Color.Black,
                )
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "내 정보",
                        style = MediCareCallTheme.typography.SB_18,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "편집",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.active,
                        modifier = Modifier.clickable(onClick = onEditClick),
                    )
                }

                SettingInfoItem("이름", name.ifEmpty { "이름 없음" })
                SettingInfoItem("생일", formatDateToKorean(birthDate.ifEmpty { "날짜 정보가 없습니다" }))
                SettingInfoItem("성별", genderText)
                SettingInfoItem("휴대폰번호", formatPhoneNumber(phone.ifEmpty { "전화번호 정보가 없습니다" }))
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                        .clickable(onClick = onLogoutClick),
                )
                Text(
                    text = "서비스 탈퇴",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onServiceWithdrawClick),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onDismiss = onLogoutDismiss,
            onLogout = onLogoutConfirm,
        )
    }
}

fun formatPhoneNumber(number: String): String {
    return number.replaceFirst(
        "(\\d{3})(\\d{4})(\\d{4})".toRegex(),
        "$1-$2-$3",
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsMyDataLayoutPreview() {
    MediCareCallTheme {
        SettingsMyDataLayout(
            name = "홍길동",
            birthDate = "1990-01-01",
            gender = GenderType.MALE,
            phone = "01012345678",
            showLogoutDialog = false,
            onBackClick = {},
            onEditClick = {},
            onLogoutClick = {},
            onServiceWithdrawClick = {},
            onLogoutDismiss = {},
            onLogoutConfirm = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsMyDataLayoutEmptyPreview() {
    MediCareCallTheme {
        SettingsMyDataLayout(
            name = "",
            birthDate = "",
            gender = GenderType.MALE,
            phone = "",
            showLogoutDialog = false,
            onBackClick = {},
            onEditClick = {},
            onLogoutClick = {},
            onServiceWithdrawClick = {},
            onLogoutDismiss = {},
            onLogoutConfirm = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsMyDataLayoutFemalePreview() {
    MediCareCallTheme {
        SettingsMyDataLayout(
            name = "김영희",
            birthDate = "1985-05-15",
            gender = GenderType.FEMALE,
            phone = "01098765432",
            showLogoutDialog = false,
            onBackClick = {},
            onEditClick = {},
            onLogoutClick = {},
            onServiceWithdrawClick = {},
            onLogoutDismiss = {},
            onLogoutConfirm = {},
        )
    }
}
