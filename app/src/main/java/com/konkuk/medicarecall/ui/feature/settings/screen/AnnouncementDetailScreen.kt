package com.konkuk.medicarecall.ui.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun AnnouncementDetailScreen(
    modifier: Modifier = Modifier,
    noticeInfo: NoticesResponseDto,
    onBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
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
        Column(
            modifier = modifier.verticalScroll(scrollState),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = noticeInfo.title,
                    style = MediCareCallTheme.typography.SB_16,
                    color = MediCareCallTheme.colors.black,
                    modifier = modifier.fillMaxWidth(),
                )
                Spacer(modifier = modifier.height(5.dp))
                Spacer(modifier = modifier.height(5.dp))
                Text(
                    text = noticeInfo.publishedAt.replace("-", "."),
                    style = MediCareCallTheme.typography.R_15,
                    color = MediCareCallTheme.colors.gray4,
                    modifier = modifier.fillMaxWidth(),
                )
                Spacer(modifier = modifier.height(10.dp))
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MediCareCallTheme.colors.gray1),
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = noticeInfo.contents,
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray6,
                )
            }
        }
    }
}
