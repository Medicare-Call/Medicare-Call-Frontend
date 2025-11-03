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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.component.SubscribeCard
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.SubscribeViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun SettingSubscribeScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToSubscribeDetail: (subscription: EldersSubscriptionResponseDto) -> Unit = {},
    viewModel: SubscribeViewModel = hiltViewModel(),
) {
    val eldersInfo = viewModel.subscriptions
    Log.d("SettingSubscribeScreen", "Elders Info: $eldersInfo")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "구독관리",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color.Black,
                )
            },
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = modifier.height(20.dp))
            eldersInfo.forEach {
                SubscribeCard(
                    elderInfo = it,
                    onClick = {
                        navigateToSubscribeDetail(it)
                    },
                )
            }
            Spacer(modifier = modifier.height(20.dp))
        }
    }
}
