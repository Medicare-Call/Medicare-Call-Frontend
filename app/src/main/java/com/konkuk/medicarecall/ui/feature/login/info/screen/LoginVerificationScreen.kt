package com.konkuk.medicarecall.ui.feature.login.info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginEvent
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import kotlinx.coroutines.launch

@Composable
fun LoginVerificationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToUserInfo: () -> Unit = {},
    navigateToPhone: () -> Unit = {},
    navigateToRegisterElder: () -> Unit = {},
    navigateToCareCallSetting: () -> Unit = {},
    navigateToPurchase: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val navigationDestination by loginViewModel.navigationDestination.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        loginViewModel.events.collect { event ->
            when (event) {
                is LoginEvent.VerificationSuccessNew -> {
                    // 인증 성공 시 회원정보 화면으로 이동
                    navigateToUserInfo()
                }

                is LoginEvent.VerificationSuccessExisting -> {
                    // 인증 성공, 기존 회원일 시 등록된 어르신, 시간, 결제 정보 확인
                    loginViewModel.checkStatus()
                }

                is LoginEvent.VerificationFailure -> {
                    // 인증 실패 시 스낵바 표시
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            message = "인증번호가 올바르지 않습니다",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                else -> { /* 다른 이벤트 무시 */
                }
            }
        }
    }

    LaunchedEffect(navigationDestination) {
        navigationDestination?.let { destination ->
            // 기존 사용자는 바로 회원정보 입력 화면으로 이동하지 않고 다른 처리가 필요할 수 있음
            navigateToUserInfo()
            when (destination) {
                is NavigationDestination.GoToLogin -> navigateToPhone()
                is NavigationDestination.GoToRegisterElder -> navigateToRegisterElder()
                is NavigationDestination.GoToTimeSetting -> navigateToCareCallSetting()
                is NavigationDestination.GoToPayment -> navigateToPurchase()
                is NavigationDestination.GoToHome -> navigateToHome()
            }
            loginViewModel.onNavigationHandled()
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
            .imePadding(),
    ) {
        Column {
            LoginBackButton(
                onBack,
            )
            Column(
                Modifier
                    .verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "인증번호를\n입력해주세요",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(Modifier.height(40.dp))
                DefaultTextField(
                    loginViewModel.verificationCode,
                    { input ->
                        val filtered = input.filter { it.isDigit() }.take(6)
                        loginViewModel.onVerificationCodeChanged(filtered)
                    },
                    placeHolder = "인증번호 입력",
                    keyboardType = KeyboardType.Number,
                    textFieldModifier = Modifier.focusRequester(focusRequester),
                    maxLength = 6,
                )

                Spacer(Modifier.height(30.dp))

                CTAButton(
                    type = if (loginViewModel.verificationCode.length == 6) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                    "확인",
                    onClick = {
                        // TODO: 서버에 인증번호 보내서 확인하기
                        loginViewModel.confirmPhoneNumber(
                            loginViewModel.phoneNumber,
                            loginViewModel.verificationCode,
                        )
                        loginViewModel.onVerificationCodeChanged("")
                    },
                )
            }
        }
        DefaultSnackBar(
            snackBarState,
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
        )
    }
}
