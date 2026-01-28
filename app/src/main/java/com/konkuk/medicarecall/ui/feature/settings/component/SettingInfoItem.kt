package com.konkuk.medicarecall.ui.feature.settings.component

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
fun SettingInfoItem(category: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = category,
            style = MediCareCallTheme.typography.R_14,
            color = MediCareCallTheme.colors.gray4,
        )
        Spacer(modifier = modifier.height(5.dp))
        Text(
            text = value,
            style = MediCareCallTheme.typography.R_16,
            color = MediCareCallTheme.colors.gray8,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingInfoItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            SettingInfoItem(
                category = "이름",
                value = "홍길동",
            )
            Spacer(Modifier.height(16.dp))
            SettingInfoItem(
                category = "생년월일",
                value = "1990년 1월 1일",
            )
        }
    }
}
