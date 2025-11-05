package com.konkuk.medicarecall.ui.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.request.MedicationSchedule
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.IllnessInfoItem
import com.konkuk.medicarecall.ui.common.component.MedInfoItem
import com.konkuk.medicarecall.ui.common.component.SpecialNoteItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailHealthViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import com.konkuk.medicarecall.ui.type.MedicationTimeType

@Composable
fun HealthDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    healthInfoResponseDto: EldersHealthResponseDto,
    detailViewModel: DetailHealthViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val diseaseList = remember(healthInfoResponseDto) {
        healthInfoResponseDto.diseases.toMutableStateList()
    }
    val medications = remember(healthInfoResponseDto) {
        healthInfoResponseDto.medications.toMedicationSchedules().toMutableStateList()
    }
    val noteList = remember(healthInfoResponseDto) {
        healthInfoResponseDto.notes.map { it.displayName }.toMutableStateList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "어르신 건강정보 설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color.Black,
                )
            },
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(Modifier.height(20.dp))
            // 질환 정보
            IllnessInfoItem(
                diseaseList = diseaseList,
                onAddDisease = { diseaseList.add(it) },
                onRemoveDisease = { diseaseList.remove(it) },
            )
            Spacer(modifier = modifier.height(20.dp))
            // 복약정보
            MedInfoItem(
                medications = medications,
//                onAddMedication = { medications.add(it) },
//                onRemoveMedication = { medications.remove(it) },
            )
            Spacer(modifier = modifier.height(20.dp))
            // 특이사항
            SpecialNoteItem(
                enumList = HealthIssueType.entries.map { it.displayName }.toList(),
                noteList = noteList,
                onAddNote = { noteList.add(it) },
                onRemoveNote = { noteList.remove(it) },
                placeHolder = "특이사항 선택하기",
                category = "특이사항",
                scrollState = scrollState,
            )
            Spacer(modifier = modifier.height(20.dp))
            CTAButton(
                type = CTAButtonType.GREEN,
                text = "확인",
                onClick = {
                    val noteEnums: List<HealthIssueType> = noteList.mapNotNull { display ->
                        HealthIssueType.entries.firstOrNull { it.displayName == display }
                    }
                    detailViewModel.updateElderHealth(
                        healthInfo = EldersHealthResponseDto(
                            elderId = healthInfoResponseDto.elderId,
                            name = healthInfoResponseDto.name,
                            diseases = diseaseList,
                            medications = medications.toTimeMap(),
                            notes = noteEnums,
                        ),
                    ) {
                        onBack()
                    }
                },
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
    }
}

// Map<시간대, 약리스트> -> List<MedicationSchedule> (UI용)
fun Map<MedicationTimeType, List<String>>.toMedicationSchedules(): List<MedicationSchedule> {
    val timesByMed = linkedMapOf<String, MutableSet<MedicationTimeType>>()
    for ((time, meds) in this) {
        for (med in meds) {
            val key = med.trim()
            if (key.isNotEmpty()) timesByMed.getOrPut(key) { linkedSetOf() }.add(time)
        }
    }
    return timesByMed.map { (name, times) ->
        MedicationSchedule(
            medicationName = name,
            scheduleTimes = times.sortedBy { it.ordinal },
        )
    }
}

// List<MedicationSchedule> -> Map<시간대, 약리스트> (서버에 Map 형태로 보내야 할 때 쓰기)
fun List<MedicationSchedule>.toTimeMap(): Map<MedicationTimeType, List<String>> {
    val map = linkedMapOf<MedicationTimeType, MutableList<String>>()
    for (sch in this) {
        val name = sch.medicationName.trim()
        if (name.isEmpty()) continue
        for (t in sch.scheduleTimes) {
            map.getOrPut(t) { mutableListOf() }.add(name)
        }
    }
    return map.mapValues { it.value.toList() }
}
