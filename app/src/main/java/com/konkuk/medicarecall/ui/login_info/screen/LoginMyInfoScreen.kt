package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.data.model.LoginUiState
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
    }
}