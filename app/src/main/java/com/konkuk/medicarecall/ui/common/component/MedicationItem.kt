package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.domain.model.Medication
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

// 반복되는 UI를 재사용 가능한 함수로 추출
@Composable
private fun MedicationTimeSection(
    title: String,
    medications: List<String>,
    onRemoveChip: (String) -> Unit,
) {
    // 약 목록이 비어있지 않을 때만 UI를 표시
    if (medications.isNotEmpty()) {
        Column {
            Text(
                text = title,
                color = MediCareCallTheme.colors.gray5,
                style = MediCareCallTheme.typography.R_15,
            )
            Spacer(Modifier.height(10.dp))
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // 안전한 순회를 위해 toList()로 복사본을 만들어 사용
                medications.toList().forEach { medication ->
                    ChipItem(medication) {
                        // 클릭 이벤트가 발생하면 상위로 전달
                        onRemoveChip(medication)
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItem(
    medications: List<Medication>,
    selectedTimes: List<MedicationTime>,
    inputTextState: TextFieldState,
    onSelectTime: (MedicationTime) -> Unit,
    onAddMedication: (String) -> Unit,
    onRemoveMedication: (Medication) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            "복약 정보",
            color = MediCareCallTheme.colors.gray7,
            style = MediCareCallTheme.typography.M_17,
        )
        Spacer(Modifier.height(10.dp))

        MedicationTime.entries.forEach { timeType ->
            val medsForTime = medications.filter { timeType in it.times }
            if (medsForTime.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                MedicationTimeSection(
                    title = timeType.displayName,
                    medications = medsForTime.map { it.medicine },
                    onRemoveChip = { medicineName ->
                        medications.find { it.medicine == medicineName }?.let { onRemoveMedication(it) }
                    },
                )
            }
        }

        if (medications.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MedicationTime.entries.forEach { time ->
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(
                            color = if (time in selectedTimes) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.white,
                        )
                        .border(
                            1.2.dp,
                            if (time in selectedTimes) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.gray2,
                            CircleShape,
                        )
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = { onSelectTime(time) },
                        ),
                ) {
                    Text(
                        time.displayName,
                        color = if (time in selectedTimes) MediCareCallTheme.colors.g50
                        else MediCareCallTheme.colors.gray5,
                        style = if (time in selectedTimes) MediCareCallTheme.typography.SB_14
                        else MediCareCallTheme.typography.R_14,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        AddTextField(
            textFieldState = inputTextState,
            placeHolder = "예시) 당뇨약",
            clickPlus = {
                val inputText = inputTextState.text.toString()
                if (inputText.isNotBlank() && selectedTimes.isNotEmpty()) {
                    onAddMedication(inputText)
                    inputTextState.edit { replace(0, length, "") }
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            MedicationItem(
                medications = listOf(
                    Medication("당뇨약", listOf(MedicationTime.BREAKFAST)),
                    Medication("혈압약", listOf(MedicationTime.DINNER)),
                ),
                selectedTimes = listOf(),
                inputTextState = TextFieldState(""),
                onSelectTime = {},
                onAddMedication = {},
                onRemoveMedication = {},
            )
        }
    }
}
