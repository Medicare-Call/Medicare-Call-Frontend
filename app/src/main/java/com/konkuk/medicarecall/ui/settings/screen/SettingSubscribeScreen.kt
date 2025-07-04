package com.konkuk.medicarecall.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun SettingSubscribeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().background(MediCareCallTheme.colors.bg)) {
        SettingsTopAppBar(modifier = modifier, title = "구독관리", leftIcon = {Icon(painter = painterResource(id = R.drawable.ic_settings_back), contentDescription = "go_back", modifier = modifier.size(24.dp), tint = Color.Black)}
        )
        Column(
            modifier = modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = modifier.fillMaxWidth().figmaShadow(group = MediCareCallTheme.shadow.shadow03).clip(RoundedCornerShape(14.dp))
                .background(MediCareCallTheme.colors.white)
                .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                Column(
                    modifier = modifier.width(252.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(text = "김옥자", style = MediCareCallTheme.typography.SB_16, color = MediCareCallTheme.colors.gray8) // 나중에 값받아와서 바껴야 하는 부분
                        Spacer(modifier = modifier.width(5.dp))
                        Text(text = "어르신", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                    }
                    Spacer(modifier = modifier.height(8.dp))
                    Text(text = "프리미엄 플랜 구독 중",style = MediCareCallTheme.typography.SB_18, color = MediCareCallTheme.colors.main,modifier = modifier.fillMaxWidth())
                }
                Icon(
                    contentDescription = "구독관리 자세히 보기 아이콘",
                    painter = painterResource(id = R.drawable.ic_arrow_right), modifier = modifier.size(28.dp), tint = MediCareCallTheme.colors.gray2
                )
            }
            Row(modifier = modifier.fillMaxWidth().figmaShadow(group = MediCareCallTheme.shadow.shadow03).clip(RoundedCornerShape(14.dp))
                .background(MediCareCallTheme.colors.white)
                .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier.width(252.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(text = "박막례", style = MediCareCallTheme.typography.SB_16, color = MediCareCallTheme.colors.gray8) // 나중에 값받아와서 바껴야 하는 부분
                        Spacer(modifier = modifier.width(5.dp))
                        Text(text = "어르신", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                    }
                    Spacer(modifier = modifier.height(8.dp))
                    Text(text = "프리미엄 플랜 구독 중",style = MediCareCallTheme.typography.SB_18, color = MediCareCallTheme.colors.main,modifier = modifier.fillMaxWidth())
                }
                Icon(
                    contentDescription = "구독관리 자세히 보기 아이콘",
                    painter = painterResource(id = R.drawable.ic_arrow_right), modifier = modifier.size(28.dp), tint = MediCareCallTheme.colors.gray2
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(color = Color.White)
                    .padding(start = 20.dp)
                    .clickable {} // 클릭 이벤트 추가
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "추가 아이콘",
                    modifier = Modifier.size(20.dp),
                    tint = MediCareCallTheme.colors.gray4
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "어르신 더 추가하기",
                    style = MediCareCallTheme.typography.SB_14,
                    color = MediCareCallTheme.colors.gray4
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingSubPreview() {
    SettingSubscribeScreen()
}