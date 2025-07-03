package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.background
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
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlin.text.isDigit

@Composable
fun LoginVerificationScreen(
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
            loginViewModel.updateLoginUiState(LoginUiState.EnterPhoneNumber)
            navController.popBackStack()
        })
        Spacer(Modifier.height(20.dp))
        Text(
            "인증번호를\n입력해주세요",
            style = MediCareCallTheme.typography.B_26,
            color = MediCareCallTheme.colors.black
        )
        Spacer(Modifier.height(40.dp))
        DefaultTextField(
            loginViewModel.verificationCode,
            { input ->
                val filtered = input.filter { it.isDigit() }.take(6)
                loginViewModel.onVerificationCodeChanged(filtered)
            },
            "인증번호 입력",
            KeyboardType.Number
        )

        Spacer(Modifier.height(30.dp))
        CTAButton(type = CTAButtonType.GREEN, "확인", onClick = {
            // TODO: 서버에 인증번호 보내서 확인하기
            loginViewModel.updateLoginUiState(LoginUiState.EnterMyInfo)
            navController.navigate("login_my_info")
        })

    }
}