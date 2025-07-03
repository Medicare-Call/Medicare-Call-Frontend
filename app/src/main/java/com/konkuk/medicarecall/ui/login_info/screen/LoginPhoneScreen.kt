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
import com.konkuk.medicarecall.ui.util.PhoneNumberVisualTransformation
import kotlin.text.isDigit

@Composable
fun LoginPhoneScreen(
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
            loginViewModel.updateLoginUiState(LoginUiState.Start)
            navController.popBackStack()
        })
        Spacer(Modifier.height(20.dp))
        Text(
            "휴대폰 번호를\n입력해주세요",
            style = MediCareCallTheme.typography.B_26,
            color = MediCareCallTheme.colors.black
        )
        Spacer(Modifier.height(40.dp))
        DefaultTextField(
            loginViewModel.phoneNumber,
            { input ->
                val filtered = input.filter { it.isDigit() }.take(11)
                loginViewModel.onPhoneNumberChanged(filtered)
            },
            "휴대폰 번호",
            KeyboardType.Number,
            PhoneNumberVisualTransformation()
        )

        Spacer(Modifier.height(30.dp))
        CTAButton(type = CTAButtonType.GREEN, "인증번호 받기", {
            // TODO: 서버에 인증번호 요청하기
            loginViewModel.updateLoginUiState(LoginUiState.EnterVerificationCode)
            navController.navigate("login_verification")
        })

    }
}