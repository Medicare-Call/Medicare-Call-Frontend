package com.konkuk.medicarecall.ui.feature.statistics.weeklycard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.util.WeeklySummaryUtil
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklySummaryUiState.WeeklyMedicineUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun WeeklyMedicineCard(
    modifier: Modifier = Modifier,
    medicine: List<WeeklyMedicineUiState>,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow01,
                cornerRadius = 14.dp,
            ),

        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
        ) {
            // 1) Title: 복약 통계
            Text(
                "복약통계",
                style = MediCareCallTheme.typography.R_15,
                color = MediCareCallTheme.colors.gray5,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2) 약 이름
            Column(
                verticalArrangement = Arrangement
                    .spacedBy(0.dp),
            ) {
                medicine.forEach { weeklyMedicine ->

                    val isUnrecorded =
                        weeklyMedicine.takenCount == null || weeklyMedicine.takenCount < 0 // -1과 같은 음수 값으로 미기록 판단
                    val iconColor = if (isUnrecorded) {
                        MediCareCallTheme.colors.gray2
                    } else {
                        WeeklySummaryUtil.getMedicineIconColor(
                            weeklyMedicine,
                            MediCareCallTheme.colors,
                        )
                    }

                    val takenText = if (isUnrecorded) {
                        "- / ${weeklyMedicine.totalCount}"
                    } else {
                        "${weeklyMedicine.takenCount}/${weeklyMedicine.totalCount}"
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = weeklyMedicine.medicineName,
                            style = MediCareCallTheme.typography.R_15,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            modifier = Modifier
                                .size(16.dp),
                            painter = painterResource(id = R.drawable.ic_filledpills),
                            contentDescription = null,
                            tint = iconColor,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = takenText,
                            style = MediCareCallTheme.typography.R_14,
                            color = if (isUnrecorded) MediCareCallTheme.colors.gray4 else MediCareCallTheme.colors.gray6,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "복약 카드 - 기록 있음")
@Composable
fun PreviewWeeklyMedicineCardRecorded() {
    WeeklyMedicineCard(
        modifier = Modifier
            .size(150.dp, 140.dp),
        medicine = listOf(
            WeeklyMedicineUiState("혈압약", 0, 14),
            WeeklyMedicineUiState("영양제", 4, 7),
            WeeklyMedicineUiState("당뇨약", 21, 21),
        ),
    )
}

@Preview(name = "복약 카드 - 미기록")
@Composable
fun PreviewWeeklyMedicineCardUnRecorded() {
    WeeklyMedicineCard(
        modifier = Modifier
            .size(150.dp, 140.dp),
        medicine = listOf(
            WeeklyMedicineUiState("혈압약", -1, 14),
            WeeklyMedicineUiState("영양제", -1, 7),
            WeeklyMedicineUiState("당뇨약", -1, 21),
        ),
    )
}
