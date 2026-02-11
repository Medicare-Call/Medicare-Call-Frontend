package com.konkuk.medicarecall.ui.feature.login.calltime.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.domain.model.type.TimeSettingType

@Composable
fun TimeSettingItem(
    category: String,
    timeType: TimeSettingType,
    timeText: String?,
    modifier: Modifier = Modifier,
) {
    timeType // TODO: 현재 사용되지 않는 변수입니다. 추후 기능 추가 시 활용할 수 있습니다.
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (timeText != null) {
            Text(
                text = category,
                style = MediCareCallTheme.typography.M_17,
                color = MediCareCallTheme.colors.gray5,
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = if (timeText == null) {
                        MediCareCallTheme.colors.g50
                    } else {
                        MediCareCallTheme.colors.white
                    },
                    shape = RoundedCornerShape(14.dp),
                )
                .border(
                    width = (1.2).dp,
                    color = if (timeText == null) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3,
                    shape = RoundedCornerShape(14.dp),
                )
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (timeText == null) {
                Row(
                    modifier = modifier.padding(horizontal = (9.5).dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "추가 아이콘",
                        modifier = modifier.size(20.dp),
                        tint = MediCareCallTheme.colors.main,
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "시간 설정하기",
                        style = MediCareCallTheme.typography.B_17,
                        color = MediCareCallTheme.colors.main,
                    )
                }
            } else {
                Text(
                    text = timeText,
                    style = MediCareCallTheme.typography.M_16,
                    color = MediCareCallTheme.colors.gray9,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeSettingItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            TimeSettingItem(
                category = "1차",
                timeType = TimeSettingType.FIRST,
                timeText = null,
            )
            Spacer(Modifier.height(16.dp))
            TimeSettingItem(
                category = "2차",
                timeType = TimeSettingType.SECOND,
                timeText = "오전 10:00",
            )
        }
    }
}
