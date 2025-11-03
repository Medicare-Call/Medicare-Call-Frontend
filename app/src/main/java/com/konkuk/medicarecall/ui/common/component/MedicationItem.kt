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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.MedicationTimeType

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
    medicationSchedule: Map<MedicationTimeType, List<String>>,
    selectedList: List<MedicationTimeType>,
    inputText: String,
    onTextChange: (String) -> Unit,
    onSelectTime: (MedicationTimeType) -> Unit,
    onAddMedication: (MedicationTimeType, String) -> Unit,
    onRemoveChip: (MedicationTimeType, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // UI에 표시할 제목을 Map으로 정의하여 관리 용이성을 높임
    Column(
        modifier = modifier,
    ) {
        Text(
            "복약 정보",
            color = MediCareCallTheme.colors.gray7,
            style = MediCareCallTheme.typography.M_17,
        )
        Spacer(Modifier.height(10.dp))
        MedicationTimeType.entries.forEach { timeType ->
            // 해당 시간대에 약이 없는 경우를 안전하게 처리
            val medList = medicationSchedule[timeType]
            if (!medList.isNullOrEmpty()) {
                Spacer(Modifier.height(10.dp))
                MedicationTimeSection(
                    title = timeType.time,
                    medications = medList,
                    onRemoveChip = { medicationToRemove ->
                        onRemoveChip(timeType, medicationToRemove)
                    },
                )
            }
        }

        if (!medicationSchedule.values.all { it.isEmpty() })
            Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MedicationTimeType.entries.forEach {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(
                            color = if (it in selectedList) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.white,
                        )
                        .border(
                            1.2.dp,
                            if (it in selectedList) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.gray2,
                            CircleShape,
                        )
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                onSelectTime(it)
                            },
                        ),
                ) {
                    Text(
                        it.time,
                        color = if (it in selectedList) MediCareCallTheme.colors.g50
                        else MediCareCallTheme.colors.gray5,
                        style = if (it in selectedList) MediCareCallTheme.typography.SB_14
                        else MediCareCallTheme.typography.R_14,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        AddTextField(
            inputText,
            placeHolder = "예시) 당뇨약",
            onTextChange = { onTextChange(it) },
            clickPlus = {
                if (inputText.isNotBlank() && selectedList.isNotEmpty()) { // 입력값이 있을 때만 동작
                    selectedList.forEach { time ->
                        onAddMedication(time, inputText)
                    }

                    // 사용성 개선: 약 추가 후 입력 필드와 선택된 시간 초기화
                    onTextChange("")
                }
            },
        )
    }
}
