package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import com.konkuk.medicarecall.domain.model.Health
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun StateHealthDetailCard(
    health: HealthUiState,
    modifier: Modifier = Modifier,
) {
    val healthData = health.health
    val noSymptoms = healthData.symptoms.isEmpty()
    val noAnalysis = healthData.symptomAnalysis.isBlank()

    if (noSymptoms && noAnalysis) {
        StateHealthUnrecordedCard(modifier)
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 166.dp)
            .wrapContentHeight()
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
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            // (A) 건강징후 요약
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "건강징후 요약",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                }
                Spacer(Modifier.height(12.dp))

                if (healthData.symptoms.isEmpty()) {
                    Text(
                        text = "건강징후 기록 전이에요.",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray4,
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        healthData.symptoms.forEach { symptom ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "•",
                                    style = MediCareCallTheme.typography.M_16,
                                    color = MediCareCallTheme.colors.gray8,
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = symptom.trim(),
                                    style = MediCareCallTheme.typography.M_16,
                                    color = MediCareCallTheme.colors.gray8,
                                )
                            }
                        }
                    }
                }
            }

            // (B) 증상 분석
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "증상 분석",
                        style = MediCareCallTheme.typography.R_15,
                        color = MediCareCallTheme.colors.gray5,
                    )
                }
                Spacer(Modifier.height(12.dp))

                val analysis = healthData.symptomAnalysis.orEmpty().trim()
                if (analysis.isBlank()) {
                    Text(
                        text = "증상분석 전이에요.",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray4,
                    )
                } else {
                    Text(
                        text = analysis,
                        style = MediCareCallTheme.typography.M_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }
        }
    }
}

/**
 * 전체 미기록 전용 카드 (isRecorded == false)
 * 기존에 쓰던 디자인/문구가 있다면 이 컴포저블 안에서 구성하세요.
 */
@Composable
fun StateHealthUnrecordedCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 166.dp)
            .wrapContentHeight()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow03,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "건강징후 요약",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )
            Text(
                text = "건강징후 기록 전이에요.",
                style = MediCareCallTheme.typography.R_16,
                color = MediCareCallTheme.colors.gray4,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "증상 분석",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )
            Text(
                text = "증상분석 전이에요.",
                style = MediCareCallTheme.typography.R_16,
                color = MediCareCallTheme.colors.gray4,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStateHealthDetailCard() {
    StateHealthDetailCard(
        health = HealthUiState(
            health = Health(
                symptoms = listOf(
                    "손 떨림 증상",
                    "거동 불편",
                    "몸이 느려짐",
                ),
                symptomAnalysis = "주요 증상으로 보아 파킨슨 병이 의심돼요. 어르신과 함께 병원에 방문해 보세요.",
            ),
        ),
    )
}
