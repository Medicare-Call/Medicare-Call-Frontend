package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun HomeGlucoseLevelContainer(
    modifier: Modifier = Modifier,
    glucoseLevelAverageToday: Int?,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            // 1) Title: 혈당
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.padding(
                        start = 7.dp,
                        top = 3.dp,
                        end = 6.dp,
                        bottom = 3.dp,
                    ),
                    painter = painterResource(id = R.drawable.ic_glucose),
                    contentDescription = "glucose icon",
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    "혈당",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2) 상태
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                val isRecorded = (glucoseLevelAverageToday ?: 0) > 0
                val glucoseText = if (isRecorded) "$glucoseLevelAverageToday" else "--"
                val textColor = if (isRecorded) {
                    MediCareCallTheme.colors.gray8
                } else {
                    MediCareCallTheme.colors.gray4
                }

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = glucoseText,
                        style = MediCareCallTheme.typography.SB_22,
                        color = textColor,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "mg/dL",
                        style = MediCareCallTheme.typography.M_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "혈당 기록 있음")
@Composable
private fun PreviewHomeGlucoseLevelContainerRecorded() {
    HomeGlucoseLevelContainer(
        glucoseLevelAverageToday = 120,
        onClick = {},
    )
}

@Preview(showBackground = true, name = "혈당 기록 없음")
@Composable
private fun PreviewHomeGlucoseLevelContainerNoRecord() {
    HomeGlucoseLevelContainer(
        glucoseLevelAverageToday = 0,
        onClick = {},
    )
}
