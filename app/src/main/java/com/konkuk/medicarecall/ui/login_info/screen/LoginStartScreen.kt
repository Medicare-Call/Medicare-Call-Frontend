package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.login_info.component.CTAButton
import com.konkuk.medicarecall.ui.login_info.component.CTAButtonType
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun LoginStartScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.main)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_login_start),
            "로그인 시작 배경 이미지",
            modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.FillBounds

        )

        CTAButton(
            color = CTAButtonType.WHITE,
            "시작하기",
            {
                loginViewModel.progressLoginUiState()
                navController.navigate("login_phone")
            },
            modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .padding(horizontal = 20.dp)
        )

    }
}