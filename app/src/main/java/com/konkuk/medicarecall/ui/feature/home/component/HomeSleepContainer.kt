package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

val noFontPadding = PlatformTextStyle(includeFontPadding = false)

@Composable
fun HomeSleepContainer(
    modifier: Modifier = Modifier,
    totalSleepHours: Int,
    totalSleepMinutes: Int,
    isRecorded: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow01,
                cornerRadius = 14.dp,
            ),

        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {
            // 1) Title: 수면
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_sleep),
                    contentDescription = "sleep icon",
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    "수면",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2) 수면 시간
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val hoursText = if (isRecorded) "$totalSleepHours" else "--"
                    val minutesText = if (isRecorded) "$totalSleepMinutes" else "--"
                    val textColor =
                        if (isRecorded) MediCareCallTheme.colors.gray8 else MediCareCallTheme.colors.gray4

                    Text(
                        text = hoursText,
                        style = MediCareCallTheme.typography.SB_22.copy(
                            platformStyle = noFontPadding,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        modifier = Modifier.alignByBaseline(),
                        color = textColor,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "시간",
                        style = MediCareCallTheme.typography.R_16.copy(
                            platformStyle = noFontPadding,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        modifier = Modifier.alignByBaseline(),
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = minutesText,
                        style = MediCareCallTheme.typography.SB_22.copy(
                            platformStyle = noFontPadding,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        modifier = Modifier.alignByBaseline(),
                        color = textColor,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "분",
                        style = MediCareCallTheme.typography.R_16.copy(
                            platformStyle = noFontPadding,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both,
                            ),
                        ),
                        modifier = Modifier.alignByBaseline(),
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "수면 기록 있음")
@Composable
private fun PreviewHomeSleepContainerRecorded() {
    HomeSleepContainer(
        totalSleepHours = 8,
        totalSleepMinutes = 12,
        isRecorded = true,
        onClick = {},
    )
}

@Preview(showBackground = true, name = "수면 기록 없음")
@Composable
private fun PreviewHomeSleepContainerNoRecord() {
    HomeSleepContainer(
        totalSleepHours = 0,
        totalSleepMinutes = 0,
        isRecorded = false,
        onClick = {},
    )
}
