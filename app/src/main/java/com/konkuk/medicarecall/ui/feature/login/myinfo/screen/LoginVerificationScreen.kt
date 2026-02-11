package com.konkuk.medicarecall.ui.feature.login.myinfo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.feature.login.myinfo.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginEvent
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
    viewModel: LoginInfoViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.VerificationSuccessNew -> {
                    navigateToUserInfo()
                }

                is LoginEvent.VerificationSuccessExisting -> {
                    viewModel.checkStatus()
                }

                is LoginEvent.VerificationFailure -> {
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            message = "인증번호가 올바르지 않습니다",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(uiState.navigationDestination) {
        uiState.navigationDestination?.let { destination ->
            navigateToUserInfo()
            when (destination) {
                is NavigationDestination.GoToLogin -> navigateToPhone()
                is NavigationDestination.GoToRegisterElder -> navigateToRegisterElder()
                is NavigationDestination.GoToTimeSetting -> navigateToCareCallSetting()
                is NavigationDestination.GoToPayment -> navigateToPurchase()
                is NavigationDestination.GoToHome -> navigateToHome()
            }
            viewModel.onNavigationHandled()
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding()
            .imePadding(),
    ) {
        LoginVerificationScreenLayout(
            verificationCode = uiState.verificationCode,
            onVerificationCodeChanged = { input ->
                val filtered = input.filter { it.isDigit() }.take(6)
                viewModel.onVerificationCodeChanged(filtered)
            },
            onConfirmClick = {
                viewModel.confirmPhoneNumber(
                    uiState.userInfo.phoneNumber,
                    uiState.verificationCode,
                )
                viewModel.onVerificationCodeChanged("")
            },
            onBack = onBack,
            focusRequester = focusRequester,
        )
        DefaultSnackBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
                .padding(bottom = 14.dp),
            hostState = snackBarState,
        )
    }
}

@Composable
private fun LoginVerificationScreenLayout(
    modifier: Modifier = Modifier,
    verificationCode: String,
    onVerificationCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onBack: () -> Unit = {},
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        LoginBackButton(onBack)
        Column(
            modifier = Modifier.verticalScroll(scrollState),
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                "인증번호를\n입력해주세요",
                style = MediCareCallTheme.typography.B_26,
                color = MediCareCallTheme.colors.black,
            )
            Spacer(Modifier.height(40.dp))
            DefaultTextField(
                verificationCode,
                onVerificationCodeChanged,
                placeHolder = "인증번호 입력",
                keyboardType = KeyboardType.Number,
                textFieldModifier = Modifier.focusRequester(focusRequester),
                maxLength = 6,
            )
            Spacer(Modifier.height(30.dp))
            CTAButton(
                type = if (verificationCode.length == 6) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                "확인",
                onClick = onConfirmClick,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 600)
@Composable
private fun LoginVerificationScreenPreview() {
    MediCareCallTheme {
        LoginVerificationScreenLayout(
            verificationCode = "123456",
            onVerificationCodeChanged = {},
            onConfirmClick = {},
            onBack = {},
        )
    }
}
