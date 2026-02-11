package com.konkuk.medicarecall.ui.feature.settings.subscription.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.model.ElderSubscription
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun SubscribeCard(
    elderInfo: ElderSubscription,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .figmaShadow(group = MediCareCallTheme.shadow.shadow03)
            .clip(RoundedCornerShape(14.dp))
            .background(MediCareCallTheme.colors.white)
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = modifier.width(252.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
            ) {
                Text(
                    text = elderInfo.name,
                    style = MediCareCallTheme.typography.SB_16,
                    color = MediCareCallTheme.colors.gray8,
                ) // 나중에 값받아와서 바껴야 하는 부분
                Spacer(modifier = modifier.width(5.dp))
                Text(
                    text = "어르신",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }
            Spacer(modifier = modifier.height(8.dp))
            val planInfo = when (elderInfo.plan) {
                "메디케어콜 프리미엄 플랜" -> "프리미엄 플랜"
                else -> "베이직 플랜"
            } // 추후 수정 필요
            Text(
                text = "$planInfo 구독 중",
                style = MediCareCallTheme.typography.SB_18,
                color = MediCareCallTheme.colors.main,
                modifier = modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = modifier.weight(1f))
        Icon(
            contentDescription = "구독관리 자세히 보기 아이콘",
            painter = painterResource(id = R.drawable.ic_arrow_right),
            modifier = modifier.size(28.dp),
            tint = MediCareCallTheme.colors.gray2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubscribeCardPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            SubscribeCard(
                elderInfo = ElderSubscription(
                    elderId = 1,
                    name = "김옥자",
                    plan = "메디케어콜 프리미엄 플랜",
                    price = 50000,
                    nextBillingDate = "2024-02-15",
                    startDate = "2024-01-15",
                ),
                onClick = {},
            )
        }
    }
}
