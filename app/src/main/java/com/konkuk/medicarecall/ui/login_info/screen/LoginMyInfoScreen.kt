package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.data.model.LoginUiState
import com.konkuk.medicarecall.ui.login_info.component.CTAButton
import com.konkuk.medicarecall.ui.login_info.component.CTAButtonType
import com.konkuk.medicarecall.ui.login_info.component.DefaultTextField
import com.konkuk.medicarecall.ui.login_info.component.GenderToggleButton
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun LoginMyInfoScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {

    val loginUiState = loginViewModel.loginUiState.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        TopBar({
            loginViewModel.updateLoginUiState(LoginUiState.EnterVerificationCode)
            navController.popBackStack()
        })
        Spacer(Modifier.height(20.dp))
        Text(
            "회원 정보를\n입력해주세요",
            style = MediCareCallTheme.typography.B_26,
            color = MediCareCallTheme.colors.black
        )
        Spacer(Modifier.height(40.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "이름",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )
            DefaultTextField(
                loginViewModel.name,
                {
                    loginViewModel.onNameChanged(it)
                },
                "이름"
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "생년월일",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )
            DefaultTextField(
                loginViewModel.dateOfBirth,
                {
                    loginViewModel.onDOBChanged(it)
                },
                "YYYY / MM / DD",
                keyboardType = KeyboardType.Number
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "성별",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )
            GenderToggleButton()
        }
        Spacer(Modifier.height(30.dp))
        CTAButton(CTAButtonType.GREEN, "다음", {})

    }
}