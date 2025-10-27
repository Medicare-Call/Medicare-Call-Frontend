package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 뒤로 가기
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onBack()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_big_back),
                contentDescription = "big arrow back",
                tint = MediCareCallTheme.colors.gray3,
            )

            // 제목
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    style = MediCareCallTheme.typography.SB_20,
                    color = MediCareCallTheme.colors.gray10,
                )
            }
            Box(modifier = Modifier.size(24.dp))
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MediCareCallTheme.colors.gray2,
            thickness = 1.dp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTopAppBar() {
    TopAppBar(
        title = "식사",
        onBack = {},
    )
}
