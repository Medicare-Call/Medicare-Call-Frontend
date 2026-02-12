package com.konkuk.medicarecall.ui.feature.settings.subscription.screen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.model.ElderSubscription
import com.konkuk.medicarecall.ui.common.util.formatDateToKorean
import com.konkuk.medicarecall.ui.feature.settings.component.SettingInfoItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel.SettingsSubscriptionDetailViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import com.konkuk.medicarecall.ui.type.CTAButtonType
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsSubscriptionDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderId: Long,
    viewModel: SettingsSubscriptionDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(elderId) {
        viewModel.loadSubscriptionById(elderId)
    }

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

        when {
            uiState.isLoading && uiState.subscriptionData == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("구독정보를 불러오는 중입니다...")
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = uiState.errorMessage ?: "오류가 발생했습니다",
                        color = MediCareCallTheme.colors.negative,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CTAButton(
                        type = CTAButtonType.GREEN,
                        text = "다시 시도",
                        onClick = {
                            viewModel.loadSubscriptionById(elderId)
                        },
                    )
                }
            }

            uiState.subscriptionData != null -> {
                SubscribeDetailContent(
                    subscriptionInfo = uiState.subscriptionData!!,
                )
            }
        }
    }
}

@Composable
private fun SubscribeDetailContent(
    subscriptionInfo: ElderSubscription,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 결제 정보 컴포넌트
        Column(
            modifier = Modifier
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
            SettingInfoItem("어르신 성함", subscriptionInfo.name)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = "구독 플랜",
                    style = MediCareCallTheme.typography.R_14,
                    color = MediCareCallTheme.colors.gray4,
                )
                val planInfo = when (subscriptionInfo.plan) {
                    "메디케어콜 프리미엄 플랜" -> "프리미엄 플랜"
                    else -> "베이직 플랜"
                } // 추후 수정 필요 (서버랑 값 비교할 것)
                Text(
                    text = planInfo,
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.main,
                )
                Text(
                    text = "월 ${"%,d".format(subscriptionInfo.price)}원",
                    style = MediCareCallTheme.typography.SB_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }
            SettingInfoItem("결제 예정일", formatDateToKorean(subscriptionInfo.nextBillingDate))
            SettingInfoItem("최초 가입일", formatDateToKorean(subscriptionInfo.startDate))
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "해지하기",
                style = MediCareCallTheme.typography.SB_14,
                color = MediCareCallTheme.colors.gray3,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsSubscriptionDetailScreenPreview() {
    MediCareCallTheme {
        SettingsSubscriptionDetailScreen(
            elderId = 1,
        )
    }
}
