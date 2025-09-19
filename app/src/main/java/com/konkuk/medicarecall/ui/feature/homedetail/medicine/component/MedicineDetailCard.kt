package com.konkuk.medicarecall.ui.feature.homedetail.medicine.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.model.DoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.model.DoseStatusItem
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun MedicineDetailCard(
    medicineName: String,               // 약 이름
    todayTakenCount: Int,               // 오늘 복약 완료 횟수
    todayRequiredCount: Int,            // 목표 복약 횟수
    doseStatusList: List<DoseStatusItem>,   // 복용 상태 리스트 (초록/빨강/회색)
    modifier: Modifier = Modifier
) {
    // 이상치 방어
    val safeRequired = remember(todayRequiredCount) { todayRequiredCount.coerceAtLeast(0) }


    val renderList = remember(doseStatusList, safeRequired) {
        if (safeRequired == 0) {
            emptyList()
        } else {
            val normalized = doseStatusList.map {
                when (it.doseStatus) {
                    DoseStatus.TAKEN -> it.copy(doseStatus = DoseStatus.TAKEN)
                    DoseStatus.SKIPPED -> it.copy(doseStatus = DoseStatus.SKIPPED)
                    DoseStatus.NOT_RECORDED -> it.copy(doseStatus = DoseStatus.NOT_RECORDED)
                }
            }
            when {
                normalized.isEmpty() -> List(safeRequired) {
                    DoseStatusItem(time = "", doseStatus = DoseStatus.NOT_RECORDED)
                }

                normalized.size < safeRequired -> normalized + List(safeRequired - normalized.size) {
                    DoseStatusItem(time = "", doseStatus = DoseStatus.NOT_RECORDED)
                }

                else -> normalized.take(safeRequired)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow03,
                cornerRadius = 14.dp
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)

    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 제목 + 하루 복약 횟수
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medicineName,
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8,
                )

                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "하루 ${todayRequiredCount}회 복약",
                        style = MediCareCallTheme.typography.R_14,
                        color = MediCareCallTheme.colors.gray4,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            //복약 아이콘 리스트
            Row {
                renderList.forEach { item ->
                    val tintColor = when (item.doseStatus) {
                        DoseStatus.TAKEN -> MediCareCallTheme.colors.positive
                        DoseStatus.SKIPPED -> MediCareCallTheme.colors.negative
                        DoseStatus.NOT_RECORDED -> MediCareCallTheme.colors.gray2
                    }
                    Image(
                        painter = painterResource(R.drawable.ic_pills_basic),
                        contentDescription = "복약 상태 아이콘",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(tintColor)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicineDetailCard() {
    MedicineDetailCard(
        medicineName = "당뇨약",
        todayTakenCount = 2,
        todayRequiredCount = 3,
        doseStatusList = listOf(
            DoseStatusItem(time = "MORNING", doseStatus = DoseStatus.TAKEN),
            DoseStatusItem(time = "LUNCH", doseStatus = DoseStatus.TAKEN)
        )
    )
}

