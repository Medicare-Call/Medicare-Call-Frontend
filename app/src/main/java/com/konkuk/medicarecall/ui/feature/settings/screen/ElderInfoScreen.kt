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
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.PersonalInfoCard
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersInfoViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun ElderInfoScreen(
    onBack: () -> Unit = {},
    navigateToElderDetail: (elderInfo: EldersInfoResponseDto) -> Unit = {},
    personalViewModel: EldersInfoViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                personalViewModel.refresh() // 복귀 시 재조회
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val eldersInfo = personalViewModel.eldersInfoList
    val error = personalViewModel.errorMessage

    Log.d("PersonalInfoScreen", "Elders Info: $eldersInfo")
    Log.d("PersonalInfoScreen", "Elders Info Size: ${eldersInfo.size}")
    Log.d("PersonalInfoScreen", "Error Message: $error")
    if (eldersInfo.isEmpty() && error != null) {
        Log.e("PersonalInfoScreen", "Error loading elders info: $error")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "어르신 개인정보 설정",
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
            eldersInfo.forEach {
                PersonalInfoCard(
                    name = it.name,
                    onClick = {
                        navigateToElderDetail(it)
                    },
                )
            }
//            PersonalInfoCard("김옥자", onClick = {navController.navigate(Route.PersonalDetail.route)})
//            PersonalInfoCard("박막례", onClick = {navController.navigate(Route.PersonalDetail.route)})
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
