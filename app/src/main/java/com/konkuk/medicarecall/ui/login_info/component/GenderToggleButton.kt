package com.konkuk.medicarecall.ui.login_info.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun GenderToggleButton() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(
                BorderStroke(1.2.dp, MediCareCallTheme.colors.gray2), // 전체 테두리
                RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                MediCareCallTheme.colors.white
            ),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f),

            contentAlignment = Alignment.Center
        ) {
            Text(
                "남성",
                color = MediCareCallTheme.colors.black,
                style = MediCareCallTheme.typography.M_16,
            )
        }

        VerticalDivider(thickness = 1.2.dp, color = MediCareCallTheme.colors.gray2)

        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "여성",
                color = MediCareCallTheme.colors.black,
                style = MediCareCallTheme.typography.M_16,
            )
        }
    }
}

@Preview
@Composable
private fun GenderToggleButtonPreview() {
    GenderToggleButton()
}