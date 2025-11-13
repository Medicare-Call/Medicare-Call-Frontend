package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun NotificationIconWithBadge(
    modifier: Modifier = Modifier,
    notificationCount: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        // 1. 알림 아이콘
        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "알림",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    onClick()
                },
        )

        // 2. 알림 개수가 0보다 클 때만 배지를 표시

        if (notificationCount > 0) {
            // 99를 초과할 경우 "99"로 표시
            val displayText = if (notificationCount > 99) "99" else notificationCount.toString()

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-3).dp, y = (-6).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(MediCareCallTheme.colors.negative),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayText,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        lineHeight = 14.3.sp,
                        letterSpacing = (-0.022).sp,
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNotificationIconWithBadge() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        NotificationIconWithBadge(notificationCount = 1, onClick = {})
        NotificationIconWithBadge(notificationCount = 9, onClick = {})
        NotificationIconWithBadge(notificationCount = 12, onClick = {})
        NotificationIconWithBadge(notificationCount = 99, onClick = {})
        NotificationIconWithBadge(notificationCount = 100, onClick = {})
        NotificationIconWithBadge(notificationCount = 0, onClick = {})
    }
}
