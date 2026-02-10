package com.konkuk.medicarecall.ui.feature.homedetail.sleep.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.domain.model.Sleep
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun SleepDetailCard(
    sleeps: SleepUiState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
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
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            // 1) 총 수면 시간
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = "총 수면시간",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        val isRecorded = sleeps.sleep.bedTime != null && sleeps.sleep.wakeUpTime != null
                        Text(
                            text = if (isRecorded) "${sleeps.sleep.totalSleepHours}" else "--",
                            style = MediCareCallTheme.typography.SB_22,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "시",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isRecorded) "${sleeps.sleep.totalSleepMinutes}" else "--",
                            style = MediCareCallTheme.typography.SB_22,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "분",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                }
            }

            // 취침 + 기상
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                // 2) 취침 시간
                Column {
                    Text(
                        text = "취침 시간",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = sleeps.sleep.bedTime?.ifBlank { "오후 --:--" } ?: "오후 --:--",
                            style = MediCareCallTheme.typography.SB_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                // 3) 기상 시간
                Column {
                    Text(
                        text = "기상 시간",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = sleeps.sleep.wakeUpTime?.ifBlank { "오전 --:--" } ?: "오전 --:--",
                            style = MediCareCallTheme.typography.SB_16,
                            color = MediCareCallTheme.colors.gray8,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "기록 있음")
@Composable
fun PreviewSleepDetailCard_Recorded() {
    SleepDetailCard(
        sleeps = SleepUiState(
            sleep = Sleep(
                date = "2025-07-07",
                totalSleepHours = 8,
                totalSleepMinutes = 12,
                bedTime = "오후 10:12",
                wakeUpTime = "오전 06:00",
            ),
        ),
    )
}

@Preview(showBackground = true, name = "미기록")
@Composable
fun PreviewSleepDetailCard_Unrecorded() {
    SleepDetailCard(
        sleeps = SleepUiState(),
    )
}
