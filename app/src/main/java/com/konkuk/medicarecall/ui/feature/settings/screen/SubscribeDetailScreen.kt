package com.konkuk.medicarecall.ui.feature.settings.screen

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.ui.feature.settings.component.SettingInfoItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SubscribeDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderInfo: EldersSubscriptionResponseDto,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "구독관리",
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
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 결제 정보 컴포넌트
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .figmaShadow(group = MediCareCallTheme.shadow.shadow03)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = "결제 정보",
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8,
                )
                SettingInfoItem("어르신 성함", elderInfo.name)
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        text = "구독 플랜",
                        style = MediCareCallTheme.typography.R_14,
                        color = MediCareCallTheme.colors.gray4,
                    )
                    val planInfo = when (elderInfo.plan) {
                        "메디케어콜 프리미엄 플랜" -> "프리미엄 플랜"
                        else -> "베이직 플랜"
                    } // 추후 수정 필요 (서버랑 값 비교할 것)
                    Text(
                        text = planInfo,
                        style = MediCareCallTheme.typography.SB_18,
                        color = MediCareCallTheme.colors.main,
                    )
                    Text(
                        text = "월 ${"%,d".format(elderInfo.price)}원",
                        style = MediCareCallTheme.typography.SB_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
                SettingInfoItem("결제 예정일", formatDateToKorean(elderInfo.nextBillingDate))
                SettingInfoItem("최초 가입일", formatDateToKorean(elderInfo.startDate))
            }

            // 결제 수단 변경하기 컴포넌트
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow03,
                        cornerRadius = 14.dp,
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(color = Color.White)
                    .padding(start = 20.dp)
                    .clickable {}, // 클릭 이벤트 추가
            ) {
                Text(
                    text = "결제수단 변경하기",
                    style = MediCareCallTheme.typography.SB_14,
                    color = MediCareCallTheme.colors.gray4,
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = modifier.weight(1f))
                Text(
                    text = "해지하기",
                    style = MediCareCallTheme.typography.SB_14,
                    color = MediCareCallTheme.colors.gray3,
                )
            }
        }
    }
}

fun formatDateToKorean(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateStr // 파싱 실패 시 원래 문자열 반환
    }
}
