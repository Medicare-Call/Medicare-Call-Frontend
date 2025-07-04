package com.konkuk.medicarecall.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun MyDataSettingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "내 정보 설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    "내 정보",
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "이름",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray4
                        )
                        Text(
                            text = "김미연",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8
                        )
                    }
                    Icon(
                        contentDescription = "이동 아이콘",
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = MediCareCallTheme.colors.gray2,
                        modifier = modifier.size(28.dp)
                    )
                }

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "생일",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray4
                        )
                        Text(
                            text = "1970년 5월 29일",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8
                        )
                    }
                    Icon(
                        contentDescription = "이동 아이콘",
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = MediCareCallTheme.colors.gray2,
                        modifier = modifier.size(28.dp)
                    )
                }

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "성별",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray4
                        )
                        Text(
                            text = "여성",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8
                        )
                    }
                    Icon(
                        contentDescription = "이동 아이콘",
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = MediCareCallTheme.colors.gray2,
                        modifier = modifier.size(28.dp)
                    )
                }

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "휴대폰번호",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray4
                        )
                        Text(
                            text = "010-0000-0000",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8
                        )
                    }
                    Icon(
                        contentDescription = "이동 아이콘",
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = MediCareCallTheme.colors.gray2,
                        modifier = modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    "계정 관리",
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "로그아웃",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray8
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "화살표 아이콘",
                        modifier = Modifier.size(28.dp),
                        tint = MediCareCallTheme.colors.gray2
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "서비스 탈퇴",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray8
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "화살표 아이콘",
                        modifier = Modifier.size(28.dp),
                        tint = MediCareCallTheme.colors.gray2
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun MydataSettingPreview() {
    MyDataSettingScreen()

}
