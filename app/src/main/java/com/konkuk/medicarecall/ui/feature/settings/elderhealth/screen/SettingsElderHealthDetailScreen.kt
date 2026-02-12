package com.konkuk.medicarecall.ui.feature.settings.elderhealth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.IllnessInfoItem
import com.konkuk.medicarecall.ui.common.component.MedInfoItem
import com.konkuk.medicarecall.ui.common.component.SpecialNoteItem
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel.SettingsElderHealthDetailViewModel
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import com.konkuk.medicarecall.ui.model.MedicationSchedule
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.iterator

@Composable
fun SettingsElderHealthDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderId: Long,
    viewModel: SettingsElderHealthDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(elderId) {
        viewModel.loadHealthInfoById(elderId)
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

        when {
            uiState.isLoading && uiState.healthData == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("건강정보를 불러오는 중입니다...")
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = uiState.errorMessage ?: "오류가 발생했습니다",
                        color = MediCareCallTheme.colors.negative,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CTAButton(
                        type = CTAButtonType.GREEN,
                        text = "다시 시도",
                        onClick = {
                            viewModel.loadHealthInfoById(elderId)
                        },
                    )
                }
            }

            uiState.healthData != null -> {
                HealthDetailContent(
                    healthInfo = uiState.healthData!!,
                    viewModel = viewModel,
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
private fun HealthDetailContent(
    healthInfo: ElderHealthInfo,
    viewModel: SettingsElderHealthDetailViewModel,
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val diseaseList = remember(healthInfo) {
        healthInfo.diseases.toMutableStateList()
    }
    val medications = remember(healthInfo) {
        healthInfo.medications.toMedicationSchedules().toMutableStateList()
    }
    val noteList = remember(healthInfo) {
        healthInfo.notes.map { it.displayName }.toMutableStateList()
    }

    Column(
        modifier = Modifier
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
        Spacer(modifier = Modifier.height(20.dp))
        // 복약정보
        MedInfoItem(
            medications = medications,
        )
        Spacer(modifier = Modifier.height(20.dp))
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
        Spacer(modifier = Modifier.height(20.dp))
        CTAButton(
            type = CTAButtonType.GREEN,
            text = "확인",
            onClick = {
                val noteEnums: List<HealthIssueType> = noteList.mapNotNull { display ->
                    HealthIssueType.entries.firstOrNull { it.displayName == display }
                }
                viewModel.updateElderHealth(
                    healthInfo = ElderHealthInfo(
                        elderId = healthInfo.elderId,
                        name = healthInfo.name,
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

// Map<시간대, 약리스트> -> List<MedicationSchedule> (UI용)
fun Map<MedicationTime, List<String>>.toMedicationSchedules(): List<MedicationSchedule> {
    val timesByMed = linkedMapOf<String, MutableSet<MedicationTime>>()
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
fun List<MedicationSchedule>.toTimeMap(): Map<MedicationTime, List<String>> {
    val map = linkedMapOf<MedicationTime, MutableList<String>>()
    for (sch in this) {
        val name = sch.medicationName.trim()
        if (name.isEmpty()) continue
        for (t in sch.scheduleTimes) {
            map.getOrPut(t) { mutableListOf() }.add(name)
        }
    }
    return map.mapValues { it.value.toList() }
}

@Preview(showBackground = true)
@Composable
private fun SettingsElderHealthDetailScreenPreview() {
    MediCareCallTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg)
                .systemBarsPadding(),
        ) {
            SettingsTopAppBar(
                title = "어르신 건강정보 설정",
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_back),
                        contentDescription = "go_back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                    )
                },
            )
        }
    }
}
