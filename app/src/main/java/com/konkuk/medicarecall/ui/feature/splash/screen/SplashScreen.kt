package com.konkuk.medicarecall.ui.feature.splash.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.navigation.Route
import com.konkuk.medicarecall.ui.feature.splash.viewmodel.SplashViewModel
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun SplashScreen(navController: NavController) {

    val viewModel: SplashViewModel = hiltViewModel()

    val navigationDestination by viewModel.navigationDestination.collectAsState()

    LaunchedEffect(navigationDestination) {
        navigationDestination?.let { destination ->
            val route = when (destination) {
                is NavigationDestination.GoToLogin -> Route.LoginStart.route
                is NavigationDestination.GoToRegisterElder -> Route.LoginRegisterElder.route
                is NavigationDestination.GoToTimeSetting -> Route.LoginCareCallSetting.route
                is NavigationDestination.GoToPayment -> Route.LoginPurchase.route
                is NavigationDestination.GoToHome -> Route.Home.route

            }
            navController.navigate(Route.LoginStart.route) {
                popUpTo(Route.Splash.route) { inclusive = true }
                launchSingleTop = true
            }
            navController.navigate(route) {
                if (route == Route.Home.route) {
                    popUpTo(Route.LoginStart.route) {
                        inclusive = true
                    }
                }
                launchSingleTop = true
            }

        }
    }

    val context = LocalContext.current

    // DisposableEffect를 사용하여 화면에서 벗어날 때 원래 방향으로 복구합니다.
    DisposableEffect(Unit) {
        // 이 화면에 진입했을 때 실행될 코드
        val activity = context as? Activity
        // 현재 화면 방향을 저장해 둡니다.
        val originalOrientation = activity?.requestedOrientation
        // 화면 방향을 세로로 고정합니다.
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            // 이 화면에서 벗어날 때 실행될 코드
            // 저장해 둔 원래 방향으로 되돌립니다.
            activity?.requestedOrientation =
                originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }


    Box(
        Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.main),
    ) {
        Image(
            painterResource(R.drawable.bg_splash_new),
            "Medicare Call 스플래시",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
