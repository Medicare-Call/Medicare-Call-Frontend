package com.konkuk.medicarecall.ui.feature.settings.subscription.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.subscription.component.SubscribeCard
import com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel.SettingsSubscriptionViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsSubscriptionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToSubscribeDetail: (elderId: Long) -> Unit = {},
    viewModel: SettingsSubscriptionViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            uiState.subscriptions.forEach {
                SubscribeCard(
                    elderInfo = it,
                    onClick = {
                        navigateToSubscribeDetail(it.elderId)
                    },
                )
            }
            Spacer(modifier = modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsSubscriptionScreenPreview() {
    MediCareCallTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg)
                .statusBarsPadding(),
        ) {
            SettingsTopAppBar(
                title = "구독관리",
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_back),
                        contentDescription = "go_back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                    )
                },
            )
        }
    }
}
