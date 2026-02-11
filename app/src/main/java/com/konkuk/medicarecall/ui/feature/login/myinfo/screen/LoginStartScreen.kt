package com.konkuk.medicarecall.ui.feature.login.myinfo.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun LoginStartScreen(
    modifier: Modifier = Modifier,
    navigateToPhone: () -> Unit = {},
    navigateToRegisterElder: () -> Unit = {},
    navigateToCareCallSetting: () -> Unit = {},
    navigateToPurchase: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    loginInfoViewModel: LoginInfoViewModel = koinViewModel(),
) {
    val uiState by loginInfoViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigationDestination) {
        uiState.navigationDestination?.let { destination ->
            when (destination) {
                is NavigationDestination.GoToLogin -> navigateToPhone()
                is NavigationDestination.GoToRegisterElder -> navigateToRegisterElder()
                is NavigationDestination.GoToTimeSetting -> navigateToCareCallSetting()
                is NavigationDestination.GoToPayment -> navigateToPurchase()
                is NavigationDestination.GoToHome -> navigateToHome()
            }
            loginInfoViewModel.onNavigationHandled()
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.main)
            .navigationBarsPadding(),
    ) {
        Image(
            painter = painterResource(R.drawable.bg_login_start_new),
            "로그인 시작 배경 이미지",
            modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(top = 20.dp, start = 20.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.typo_intro),
                "AI 기반 케어콜, 부모님 건강관리는 메디케어콜",
            )
            Spacer(Modifier.height(30.dp))
            Image(
                painter = painterResource(R.drawable.typo_main),
                "메디케어콜",
            )
        }

        CTAButton(
            type = CTAButtonType.WHITE,
            "시작하기",
            {
                loginInfoViewModel.checkStatus()
            },
            modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .padding(horizontal = 20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginStartScreenPreview() {
    MediCareCallTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.main)
                .navigationBarsPadding(),
        ) {
            Image(
                painter = painterResource(R.drawable.bg_login_start_new),
                "로그인 시작 배경 이미지",
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.FillBounds,
            )

            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 20.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.typo_intro),
                    "AI 기반 케어콜, 부모님 건강관리는 메디케어콜",
                )
                Spacer(Modifier.height(30.dp))
                Image(
                    painter = painterResource(R.drawable.typo_main),
                    "메디케어콜",
                )
            }

            CTAButton(
                type = CTAButtonType.WHITE,
                "시작하기",
                {},
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 20.dp),
            )
        }
    }
}
