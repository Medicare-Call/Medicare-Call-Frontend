package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.home.viewmodel.DoseStatusUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun HomeMedicineContainer(
    modifier: Modifier = Modifier,
    medicines: List<MedicineUiState>,
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
            // Title: 복약
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_pills),
                    contentDescription = "pills icon",
                )
                Spacer(Modifier.width(4.dp))

                Text(
                    "복약",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                // 전체 복약 상태
                val totalTaken = medicines.sumOf { it.todayTakenCount ?: 0 }
                val totalRequired = medicines.sumOf { it.todayRequiredCount ?: 0 }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = "$totalTaken/$totalRequired",
                        style = MediCareCallTheme.typography.SB_22,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "회 하루 복용",
                        style = MediCareCallTheme.typography.M_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            medicines.forEachIndexed { idx, medicine ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        // 복약 이름
                        Text(
                            text = medicine.medicineName,
                            style = MediCareCallTheme.typography.SB_18,
                            color = MediCareCallTheme.colors.gray6,
                        )
                        // 다음 복약 시간
                        if (!medicine.nextDoseTime.isNullOrBlank()) { // 널이 아닐 때만 표시
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "다음 복약 : ${medicine.nextDoseTime}",
                                style = MediCareCallTheme.typography.R_14,
                                color = MediCareCallTheme.colors.main,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        // 복약 아이콘 리스트
                        val requiredCount = (medicine.todayRequiredCount ?: 0).coerceAtLeast(0)
                        val renderList = if (medicine.doseStatusList.isNullOrEmpty()) {
                            List(requiredCount) { null }
                        } else {
                            val filled = medicine.doseStatusList.map { it.taken }
                            if (filled.size < requiredCount) {
                                filled + List(requiredCount - filled.size) { null }
                            } else {
                                filled.take(requiredCount)
                            }
                        }

                        renderList.forEach { taken ->
                            val iconRes = when (taken) {
                                true -> R.drawable.ic_pill_taken
                                false -> R.drawable.ic_pill_untaken
                                null -> R.drawable.ic_pill_uncheck
                            }

                            Image(
                                painter = painterResource(iconRes),
                                contentDescription = "복약 상태 아이콘",
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${medicine.todayTakenCount ?: 0}/${medicine.todayRequiredCount ?: 0}회 복용",
                        style = MediCareCallTheme.typography.R_14,
                        color = MediCareCallTheme.colors.gray5,
                    )
                }

                if (idx < medicines.lastIndex) {
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "복약 기록 있음")
@Composable
private fun PreviewHomeMedicineContainer() {
    val sampleMedicines = listOf(
        MedicineUiState(
            medicineName = "당뇨약",
            todayTakenCount = 1,
            todayRequiredCount = 3,
            nextDoseTime = "저녁",
            doseStatusList = listOf(
                DoseStatusUiState("아침", true),
                DoseStatusUiState("점심", false),
                DoseStatusUiState("저녁", null),
            ),
        ),
        MedicineUiState(
            medicineName = "혈압약",
            todayTakenCount = 2,
            todayRequiredCount = 2,
            nextDoseTime = "아침",
            doseStatusList = listOf(
                DoseStatusUiState("아침", true),
                DoseStatusUiState("저녁", true),
            ),
        ),
    )
    HomeMedicineContainer(medicines = sampleMedicines, onClick = {})
}

@Preview(showBackground = true, name = "복약 미기록")
@Composable
private fun PreviewHomeMedicineContainerUnrecorded() {
    val sampleMedicines = listOf(
        MedicineUiState(
            "당뇨약",
            0,
            3,
            "아침",
            doseStatusList = null,
        ),
        MedicineUiState(
            "혈압약",
            0,
            2,
            "아침",
            doseStatusList = emptyList(),
        ),
    )
    HomeMedicineContainer(medicines = sampleMedicines, onClick = {})
}
