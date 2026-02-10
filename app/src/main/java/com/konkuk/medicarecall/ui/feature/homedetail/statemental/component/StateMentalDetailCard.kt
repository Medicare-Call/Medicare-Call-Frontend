package com.konkuk.medicarecall.ui.feature.homedetail.statemental.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.konkuk.medicarecall.domain.model.Mental
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun StateMentalDetailCard(
    mental: MentalUiState,
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
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // 심리상태 요약
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "심리상태 요약",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (mental.mental.mentalSummary.isEmpty()) {
                        Text(
                            text = "건강징후 기록 전이에요.",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray4,
                        )
                    } else {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            mental.mental.mentalSummary.forEach { mentalSummary ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "•",
                                        style = MediCareCallTheme.typography.M_16,
                                        color = MediCareCallTheme.colors.gray8,
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = mentalSummary,
                                        style = MediCareCallTheme.typography.M_16,
                                        color = MediCareCallTheme.colors.gray8,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStateMentalDetailCard() {
    StateMentalDetailCard(
        mental = MentalUiState(
            mental = Mental(
                mentalSummary = listOf(
                    "날씨가 좋아서 기분이 좋음",
                    "여느 때와 비슷함",
                ),
            ),
        ),
    )
}
