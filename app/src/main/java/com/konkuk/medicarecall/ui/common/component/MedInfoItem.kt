package com.konkuk.medicarecall.ui.common.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.model.MedicationSchedule
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.domain.model.type.MedicationTime

// 중복, Legacy
@Composable
fun MedInfoItem(
    medications: MutableList<MedicationSchedule>,
//    onAddMedication: (MedicationSchedule) -> Unit,
//    onRemoveMedication: (MedicationSchedule) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val inputTextState = remember { TextFieldState("") }
    // 복약 주기 선택 상태
    val selectedPeriods = remember { mutableStateOf(setOf<MedicationTime>()) }
    // 주기별 복약 리스트
//    val medsByPeriod = remember {
//        mutableStateMapOf<MedicationTimeType, SnapshotStateList<String>>().apply {
//            MedicationTimeType.entries.forEach { period ->
//                // 각 주기별로 'mutableStateListOf()'를 할당
//                this[period] = mutableStateListOf()
//            }
//        }
//    }
    val medsByPeriod = remember {
        derivedStateOf {
            MedicationTime.entries.associateWith { period ->
                medications
                    .filter { period in it.scheduleTimes }
                    .map { it.medicationName }
                    .distinct()
            }
        }
    }.value

    // 모든 주기의 리스트 중 하나라도 비어 있지 않은지 체크
    val hasAnyMeds = medsByPeriod.values.any { it.isNotEmpty() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "복약 정보",
            style = MediCareCallTheme.typography.M_17,
            color = MediCareCallTheme.colors.gray7,
        )
        MedicationTime.entries.forEach { period ->
            val list = medsByPeriod[period]!!
            if (list.isNotEmpty()) {
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    period.displayName,
                    style = MediCareCallTheme.typography.R_15,
                    color = MediCareCallTheme.colors.gray5,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 16.dp, top = 10.dp),
                ) {
                    list.forEach { name ->
                        ChipItem(
                            text = name,
                            onRemove = {
                                medications.removeOnePeriod(name, period)
                            },
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(if (hasAnyMeds) 20.dp else 10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        ) {
            MedicationTime.entries.forEach { period ->
                val selected = period in selectedPeriods.value
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(
                            if (selected) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.white,
                        )
                        .border(
                            width = 1.2.dp,
                            color = if (selected) MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.gray2,
                            shape = CircleShape,
                        )
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                val new = selectedPeriods.value.toMutableSet()
                                if (selected) new.remove(period) else new += period
                                selectedPeriods.value = new
                            },
                        ),
                ) {
                    Text(
                        text = period.displayName,
                        color = if (selected) MediCareCallTheme.colors.g50
                        else MediCareCallTheme.colors.gray5,
                        style = if (selected) {
                            MediCareCallTheme.typography.SB_14
                        } else {
                            MediCareCallTheme.typography.R_14
                        },
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 24.dp,
                        ),
                    )
                }
//
//                OutlinedButton(
//                    onClick = {
//
//                    },
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        containerColor = if (selected) MediCareCallTheme.colors.main else MediCareCallTheme.colors.white,
//                        contentColor = if (selected) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.gray5,
//
//                        ),
//                    shape = RoundedCornerShape(100.dp),
//                    border = BorderStroke(
//                        width = if (selected) 0.dp else (1.2).dp,
//                        color = MediCareCallTheme.colors.gray2
//                    ),
//                ) {
//                    Text(text = period.time, style = MediCareCallTheme.typography.R_14)
//                }
                Spacer(modifier = Modifier.width(8.dp)) // 버튼 간격
            }
        }
        // 입력필드 +  추가 버튼
        AddTextField(
            textFieldState = inputTextState,
            placeHolder = "예시) 당뇨약",
            clickPlus = {
                val name = inputTextState.text.toString().trim()
                val times = selectedPeriods.value
                if (name.isNotBlank() && times.isNotEmpty()) {
                    medications.addOrMerge(name, times)
                    inputTextState.edit { replace(0, length, "") }
                } else if (name.isNotBlank()) {
                    Toast.makeText(context, "복약 주기를 선택하세요", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }
}

private fun MutableList<MedicationSchedule>.addOrMerge(
    name: String,
    times: Set<MedicationTime>,
) {
    val idx = indexOfFirst { it.medicationName == name }
    if (idx >= 0) {
        val current = this[idx]
        val merged = (current.scheduleTimes + times).distinct()
        this[idx] = current.copy(scheduleTimes = merged)
    } else {
        add(MedicationSchedule(medicationName = name, scheduleTimes = times.toList()))
    }
}

private fun MutableList<MedicationSchedule>.removeOnePeriod(
    name: String,
    period: MedicationTime,
) {
    val idx = indexOfFirst { it.medicationName == name }
    if (idx >= 0) {
        val current = this[idx]
        val remain = current.scheduleTimes.filterNot { it == period }
        if (remain.isEmpty()) removeAt(idx)
        else this[idx] = current.copy(scheduleTimes = remain)
    }
}

@Preview(showBackground = true)
@Composable
private fun MedInfoItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            MedInfoItem(
                medications = mutableListOf(
                    MedicationSchedule(
                        medicationName = "당뇨약",
                        scheduleTimes = listOf(MedicationTime.BREAKFAST, MedicationTime.DINNER),
                    ),
                ),
            )
        }
    }
}
