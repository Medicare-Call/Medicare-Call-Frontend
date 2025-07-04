package com.konkuk.medicarecall.ui.settings.screen

import android.R.attr.text
import androidx.compose.foundation.Image
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
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
    ){
        SettingsTopAppBar(title = "설정") // 상단 TopAppBar,
        Spacer(modifier = Modifier.height(20.dp))
        Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {

        // 프로필
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
            ) {
            Image(
                painter = painterResource(id = R.drawable.img_setting_profile),
                contentDescription = "settings profile image",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "김미연",
                style = MediCareCallTheme.typography.SB_18
            ) // 나중에 값 받아와서 이름 출력되도록 수정 필요
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "님", style = MediCareCallTheme.typography.R_18)
            Spacer(modifier = Modifier.width(109.dp))
            Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(28.dp), tint = MediCareCallTheme.colors.gray2)

        }
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(modifier = Modifier.fillMaxWidth().figmaShadow(
                    group = MediCareCallTheme.shadow.shadow03,
                    cornerRadius = 14.dp
                )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white).padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_announcement),
                            contentDescription = "공지사항 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "공지사항",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_service_center),
                            contentDescription = "고객센터 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "고객센터",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_subscription_management),
                            contentDescription = "구독관리 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "구독관리",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_payment_detail),
                            contentDescription = "결제내역 아이콘",
                            modifier = Modifier.size(32.dp),
                            tint = MediCareCallTheme.colors.main
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "결제내역",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp
                    )
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCareCallTheme.colors.white)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().clickable(onClick = {})
                    ) {
                        Text(text = "어르신 개인정보 설정", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(24.dp), tint = MediCareCallTheme.colors.gray8)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().clickable(onClick = {})
                    ) {
                        Text(text = "어르신 개인정보 설정", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(24.dp), tint = MediCareCallTheme.colors.gray8)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().clickable(onClick = {}),
                    ) {
                        Text(text = "어르신 건강정보 설정", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(24.dp), tint = MediCareCallTheme.colors.gray8)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "케어콜 스케줄 설정", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(24.dp), tint = MediCareCallTheme.colors.gray8)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "푸시 알림 설정", style = MediCareCallTheme.typography.R_16, color = MediCareCallTheme.colors.gray8)
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = "화살표 아이콘", modifier = Modifier.size(24.dp), tint = MediCareCallTheme.colors.gray8)
                    }

                }
            }
        }
    }
    }


@Preview(showBackground = true)
@Composable
private fun SettingPreview() {
    SettingsScreen()
    
}