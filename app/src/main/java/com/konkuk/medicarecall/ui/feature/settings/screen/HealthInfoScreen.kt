package com.konkuk.medicarecall.ui.feature.settings.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.PersonalInfoCard
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersHealthViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun HealthInfoScreen(
    onBack: () -> Unit = {},
    navigateToHealthDetail: (EldersHealthResponseDto) -> Unit = {},
    healthInfoViewModel: EldersHealthViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                healthInfoViewModel.refresh() // 복귀 시 재조회
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val healthInfo = healthInfoViewModel.eldersInfoList
    val error = healthInfoViewModel.errorMessage

    Log.d("HealthInfoScreen", "어르신 건강정보 수: ${healthInfo.size}")
    Log.d("HealthInfoScreen", "Error Message: $error")
    Log.d("HealthInfoScreen", "Elders Info: $healthInfo")
    if (healthInfo.isEmpty() && error != null) {
        Log.e("HealthInfoScreen", "Error fetching elders info: $error")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "어르신 건강정보 설정",
            leftIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "setting back",
                    modifier = Modifier.clickable { onBack() },
                    tint = MediCareCallTheme.colors.black,
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            healthInfo.forEach {
                PersonalInfoCard(
                    name = it.name,
                    onClick = {
                        navigateToHealthDetail(it)
                    },
                )
            }
//            PersonalInfoCard("김옥자",  onClick = {navController.navigate(Route.HealthDetail.route)})
//            PersonalInfoCard("박막례",  onClick = {navController.navigate(Route.HealthDetail.route)})
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
