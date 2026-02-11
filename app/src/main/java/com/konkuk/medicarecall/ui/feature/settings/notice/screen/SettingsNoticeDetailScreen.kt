package com.konkuk.medicarecall.ui.feature.settings.notice.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.notice.viewmodel.SettingsNoticeDetailViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsNoticeDetailScreen(
    modifier: Modifier = Modifier,
    noticeId: Long,
    onBack: () -> Unit = {},
    detailViewModel: SettingsNoticeDetailViewModel = koinViewModel(),
) {
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(noticeId) {
        detailViewModel.loadNoticeById(noticeId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "공지사항",
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

        when {
            uiState.isLoading && uiState.noticeData == null -> {
                // Loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("공지사항을 불러오는 중입니다...")
                }
            }

            uiState.errorMessage != null -> {
                // Error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = uiState.errorMessage ?: "오류가 발생했습니다",
                        color = MediCareCallTheme.colors.negative,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CTAButton(
                        type = CTAButtonType.GREEN,
                        text = "다시 시도",
                        onClick = {
                            detailViewModel.loadNoticeById(noticeId)
                        },
                    )
                }
            }

            uiState.noticeData != null -> {
                // Data loaded state
                AnnouncementDetailContent(
                    noticeInfo = uiState.noticeData!!,
                )
            }
        }
    }
}

@Composable
private fun AnnouncementDetailContent(noticeInfo: NoticesResponseDto) {
    val contents = noticeInfo.contents.replace("\\n", "\n")
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp),
        ) {
            Text(
                text = noticeInfo.title,
                style = MediCareCallTheme.typography.SB_16,
                color = MediCareCallTheme.colors.black,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = noticeInfo.publishedAt.replace("-", "."),
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray4,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MediCareCallTheme.colors.gray1),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = contents,
                style = MediCareCallTheme.typography.R_16,
                color = MediCareCallTheme.colors.gray6,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsNoticeDetailScreenPreview() {
    MediCareCallTheme {
        SettingsNoticeDetailScreen(
            noticeId = 1,
        )
    }
}
