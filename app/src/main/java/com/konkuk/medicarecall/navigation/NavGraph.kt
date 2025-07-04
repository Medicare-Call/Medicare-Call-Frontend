package com.konkuk.medicarecall.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.konkuk.medicarecall.ui.login_info.uistate.LoginState
import com.konkuk.medicarecall.ui.home.screen.HomeScreen
import com.konkuk.medicarecall.ui.login_info.screen.LoginMyInfoScreen
import com.konkuk.medicarecall.ui.login_info.screen.LoginPhoneScreen
import com.konkuk.medicarecall.ui.login_info.screen.LoginStartScreen
import com.konkuk.medicarecall.ui.login_info.screen.LoginVerificationScreen
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.settings.screen.SettingsScreen
import com.konkuk.medicarecall.ui.statistics.screen.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier) {
    val loginState = loginViewModel.loginState.collectAsState()
    val startDestination = if (loginState.value == LoginState.LoggedIn) "main" else "login"
    NavHost(
        navController = navController,
        startDestination = startDestination, // 시작 화면
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        // 메인 내비게이션
        navigation(startDestination = Route.Home.route, route = "main") {
            // 홈
            composable(route = Route.Home.route) {
                HomeScreen()
            }
            // 통계
            composable(route = Route.Statistics.route) {
                StatisticsScreen()
            }
            // 설정
            composable(route = Route.Settings.route) {
                SettingsScreen()
            }
        }

        navigation(startDestination = Route.LoginStart.route, route = "login") {
            composable(route = Route.LoginStart.route) {
                LoginStartScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginPhone.route) {
                LoginPhoneScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginVerification.route) {
                LoginVerificationScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginMyInfo.route) {
                LoginMyInfoScreen(navController, loginViewModel)
            }
        }
    }

}
