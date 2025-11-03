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
import com.konkuk.medicarecall.ui.common.util.PhoneNumberVisualTransformation
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import kotlinx.coroutines.launch

@Composable
fun LoginPhoneScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToVerification: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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
            LoginBackButton(onBack)
            Column(
                modifier = Modifier.verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "휴대폰 번호를\n입력해주세요",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(Modifier.height(40.dp))
                DefaultTextField(
                    loginViewModel.phoneNumber,
                    { input ->
                        val filtered = input.filter { it.isDigit() }.take(11)
                        loginViewModel.onPhoneNumberChanged(filtered)
                    },
                    placeHolder = "휴대폰 번호",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = PhoneNumberVisualTransformation(),
                    textFieldModifier = Modifier
                        .focusRequester(focusRequester),
                    maxLength = 11,
                )

                Spacer(Modifier.height(30.dp))
                CTAButton(
                    type = if (loginViewModel.phoneNumber.length == 11) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                    "인증번호 받기",
                    {
                        // TODO: 서버에 인증번호 요청하기
                        if (loginViewModel.phoneNumber.startsWith("010")) {
                            loginViewModel.postPhoneNumber(loginViewModel.phoneNumber)
                            navigateToVerification()
                        } else {
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "휴대폰 번호를 다시 확인해주세요",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        }
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
