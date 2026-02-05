package com.konkuk.medicarecall.ui.feature.settings.notice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun AnnouncementCard(
    title: String,
    date: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() })
            .background(color = MediCareCallTheme.colors.bg)
            .padding(20.dp),
    ) {
        Text(
            text = title,
            style = MediCareCallTheme.typography.SB_16,
            color = MediCareCallTheme.colors.black,
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = date,
            style = MediCareCallTheme.typography.R_15,
            color = MediCareCallTheme.colors.gray4,
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MediCareCallTheme.colors.gray2),
    )
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementCardPreview() {
    MediCareCallTheme {
        Column {
            AnnouncementCard(
                title = "서비스 업데이트 안내",
                date = "2024.01.15",
                onClick = {},
            )
            AnnouncementCard(
                title = "새해 인사",
                date = "2024.01.01",
                onClick = {},
            )
        }
    }
}
