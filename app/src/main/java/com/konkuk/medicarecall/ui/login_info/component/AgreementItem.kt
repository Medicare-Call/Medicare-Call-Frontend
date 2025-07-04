package com.konkuk.medicarecall.ui.login_info.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun AgreementItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                painterResource(R.drawable.ic_check_box),
                contentDescription = "체크박스",
                tint = if (isChecked) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onCheckedChange,
                )
            )
            Text(
                text,
                color = MediCareCallTheme.colors.gray9,
                style = MediCareCallTheme.typography.M_16
            )
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MediCareCallTheme.colors.negative.copy(alpha = 0.2f))
            ) {
                Text(
                    "필수", // 추후 필수 아닌 선택 동의 추가 가능
                    color = MediCareCallTheme.colors.negative,
                    style = MediCareCallTheme.typography.R_14,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Icon(
            painterResource(R.drawable.ic_right_arrow),
            contentDescription = "약관 보기",
            tint = MediCareCallTheme.colors.gray3
        )
    }
}