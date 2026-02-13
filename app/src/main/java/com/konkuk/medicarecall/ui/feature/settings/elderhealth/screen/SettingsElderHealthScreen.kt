package com.konkuk.medicarecall.ui.feature.settings.elderhealth.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.settings.component.PersonalInfoCard
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel.SettingsEldersHealthViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsElderHealthScreen(
    onBack: () -> Unit = {},
    navigateToHealthDetail: (Long) -> Unit = {},
    viewModel: SettingsEldersHealthViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            uiState.eldersInfoList.forEach {
                PersonalInfoCard(
                    name = it.name,
                    onClick = {
                        navigateToHealthDetail(it.elderId)
                    },
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsElderHealthScreenPreview() {
    MediCareCallTheme {
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
                PersonalInfoCard(name = "김옥자", onClick = {})
                PersonalInfoCard(name = "박막례", onClick = {})
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
