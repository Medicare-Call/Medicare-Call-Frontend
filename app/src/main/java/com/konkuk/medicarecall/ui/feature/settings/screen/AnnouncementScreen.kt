package com.konkuk.medicarecall.ui.feature.settings.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.AnnouncementCard
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.NoticeViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnnouncementScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToNoticeDetail: (noticeId: Int) -> Unit = {},
    viewModel: NoticeViewModel = koinViewModel(),
) {
    val scrollState = rememberScrollState()
    val notices by viewModel.noticeList.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()

    Log.d("AnnouncementScreen(notice)", "현재 공지 수: ${notices.size}")
    error?.let {
        Log.d("AnnouncementScreen(notice)", "에러 메시지: $it")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
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
        Column(
            modifier = modifier.verticalScroll(scrollState),
        ) {
            if (error != null) {
                AnnouncementCard("공지사항 오류 발생", error ?: "", onClick = {})
            } else {
                notices.forEach { notice ->
                    AnnouncementCard(
                        title = notice.title,
                        date = notice.publishedAt.replace("-", "."),
                        onClick = {
                            Log.d("AnnouncementScreen", "공지사항 클릭: ${notice.title}")
                            navigateToNoticeDetail(notice.id)
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementScreenPreview() {
    MediCareCallTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg)
                .statusBarsPadding(),
        ) {
            SettingsTopAppBar(
                title = "공지사항",
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_back),
                        contentDescription = "go_back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { },
                        tint = Color.Black,
                    )
                },
            )
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AnnouncementCard("서비스 업데이트 안내", "2024.01.15", onClick = {})
                AnnouncementCard("새해 인사", "2024.01.01", onClick = {})
            }
        }
    }
}
