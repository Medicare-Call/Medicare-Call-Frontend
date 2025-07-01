package com.konkuk.medicarecall.ui.login_info.screen

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.data.model.LoginUiState
import com.konkuk.medicarecall.ui.login_info.component.CTAButton
import com.konkuk.medicarecall.ui.login_info.component.CTAButtonType
import com.konkuk.medicarecall.ui.login_info.component.PhoneNumberTextField
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.login_info.component.VerificationCodeTextField
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

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
            loginViewModel.rollbackLoginUiState()
            navController.popBackStack() })
        Spacer(Modifier.height(20.dp))
        Text(
            if (loginUiState.value == LoginUiState.EnterPhoneNumber)
                "휴대폰 번호를\n입력해주세요"
            else
                "인증번호를\n입력해주세요",
            style = MediCareCallTheme.typography.B_26,
            color = MediCareCallTheme.colors.black
        )
        Spacer(Modifier.height(40.dp))
        if (loginUiState.value == LoginUiState.EnterPhoneNumber)
            PhoneNumberTextField(
                loginViewModel.phoneNumber,
                { loginViewModel.onPhoneNumberChanged(it) },
            )
        else
            VerificationCodeTextField(
                loginViewModel.verificationCode,
                { loginViewModel.onVerificationCodeChanged(it) },
            )

        Spacer(Modifier.height(30.dp))
        if (loginUiState.value == LoginUiState.EnterPhoneNumber)
            CTAButton(color = CTAButtonType.GREEN, "인증번호 받기", {
                // TODO: 서버에 인증번호 요청하기
                loginViewModel.progressLoginUiState()
            })
        else
            CTAButton(color = CTAButtonType.GREEN, "확인", onClick = {
                // TODO: 서버에 인증번호 보내서 확인하기
                loginViewModel.progressLoginUiState()
                // TODO: navigation 이동
            })

    }
}