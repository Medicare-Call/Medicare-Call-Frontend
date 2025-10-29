package com.konkuk.medicarecall

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.feature.login.senior.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.navigation.NavGraph
import com.konkuk.medicarecall.ui.navigation.component.MainBottomBar
import com.konkuk.medicarecall.ui.navigation.component.MainTab
import com.konkuk.medicarecall.ui.navigation.rememberMainNavigator
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        installSplashScreen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            )
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        if (Build.VERSION.SDK_INT >= 35) {
            window.isNavigationBarContrastEnforced = false
        } else {
            window.isNavigationBarContrastEnforced = false
            @Suppress("DEPRECATION")
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
        }

        setContent {
            val navigator = rememberMainNavigator()

            MediCareCallTheme {
                val loginViewModel: LoginViewModel = hiltViewModel()
                val loginElderViewModel: LoginElderViewModel = hiltViewModel()

                Scaffold(
                    modifier = Modifier.background(MediCareCallTheme.colors.bg),
                    contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
                    bottomBar = {
                        MainBottomBar(
                            visible = navigator.shouldShowBottomBar(),
                            tabs = MainTab.entries.toList(),
                            currentTab = navigator.currentTab,
                            onTabSelected = {
                                navigator.navigateToMainTab(it)
                            },
                        )
                    },
                ) { innerPadding ->
                    NavGraph(
                        navigator = navigator,
                        loginViewModel = loginViewModel,
                        loginElderViewModel = loginElderViewModel,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    )
                }
            }
        }
    }
}
