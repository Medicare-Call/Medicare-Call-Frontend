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
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun HomeMedicineContainer(
    modifier: Modifier = Modifier,
    medicines: List<MedicineUiState>,
    onClick: () -> Unit
) {


    Card(
        modifier = Modifier
            .clickable { onClick() }
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
            //1) Title: 복약
            Row(
                verticalAlignment = Alignment.CenterVertically

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
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.main,
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            //2) 복약 횟수


            // 전체
            Column(
                modifier = Modifier
                    .fillMaxWidth(),

                ) {
                val totalTaken = medicines.sumOf { it.todayTakenCount }
                val totalRequired = medicines.sumOf { it.todayRequiredCount }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$totalTaken/$totalRequired",
                        style = MediCareCallTheme.typography.SB_22,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "회 하루 복용",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray6,
                    )

                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            medicines.forEachIndexed { index, medicine ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = medicine.medicineName,
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${medicine.todayTakenCount}/${medicine.todayRequiredCount}",
                            style = MediCareCallTheme.typography.SB_22,
                            color = MediCareCallTheme.colors.gray8,
                        )
                        Text(
                            text = "회 복용",
                            style = MediCareCallTheme.typography.R_16,
                            color = MediCareCallTheme.colors.gray6,
                        )
                    }

                    if (!medicine.nextDoseTime.isNullOrBlank() && medicine.nextDoseTime != "-") {
                        Text(
                            text = "다음 복약 : ${medicine.nextDoseTime}약",
                            style = MediCareCallTheme.typography.R_14,
                            color = MediCareCallTheme.colors.gray4,
                        )
                    }
                }
                if (index < medicines.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "복약 기록 있음")
@Composable
private fun PreviewHomeMedicineContainer() {
    val sampleMedicines = listOf(
        MedicineUiState("당뇨약", 1, 3, "점심"),
        MedicineUiState("혈압약", 2, 2, "아침")
    )
    HomeMedicineContainer(medicines = sampleMedicines, onClick = {})
}

@Preview(showBackground = true, name = "복약 미기록")
@Composable
private fun PreviewHomeMedicineContainerUnrecorded() {

    val sampleMedicines = listOf(
        MedicineUiState("당뇨약", 0, 3, "아침"),
        MedicineUiState("혈압약", 0, 2, "아침")
    )
    HomeMedicineContainer(medicines = sampleMedicines, onClick = {})
}
