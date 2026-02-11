package com.konkuk.medicarecall.ui.feature.login.elder.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle

import com.konkuk.medicarecall.domain.model.Medication
import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.ChipItem
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DiseaseNamesItem
import com.konkuk.medicarecall.ui.common.component.MedicationItem
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderEvent
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderUiState
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.feature.login.myinfo.component.LoginBackButton
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginElderMedInfoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToCareCallSetting: () -> Unit = {},
    viewModel: LoginElderViewModel = koinViewModel(),
) {
    val uiState by viewModel.loginElderUiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val snackBarState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    LoginElderEvent.NavigateToCareCallSetting -> navigateToCareCallSetting()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        LoginElderMedInfoScreenLayout(
            uiState = uiState,
            scrollState = scrollState,
            onSelectElder = viewModel::setSelectedIndex,
            onRemoveDisease = viewModel::removeDisease,
            onAddDisease = viewModel::addDisease,
            onRemoveMedication = viewModel::removeMedication,
            onSelectTime = viewModel::selectMedicationTime,
            onAddMedication = viewModel::addMedication,
            onRemoveHealthNote = viewModel::removeHealthNote,
            onAddHealthNote = viewModel::addHealthNote,
            onNextClick = viewModel::postElderHealthInfoBulk,
            onBack = onBack,
        )
        DefaultSnackBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            hostState = snackBarState,
        )
    }
}

@Composable
private fun LoginElderMedInfoScreenLayout(
    modifier: Modifier = Modifier,
    uiState: LoginElderUiState = LoginElderUiState(),
    scrollState: ScrollState,
    onSelectElder: (Int) -> Unit,
    onRemoveDisease: (String) -> Unit,
    onAddDisease: (String) -> Unit,
    onRemoveMedication: (Medication) -> Unit,
    onSelectTime: (MedicationTime) -> Unit,
    onAddMedication: (String) -> Unit,
    onRemoveHealthNote: (HealthIssueType) -> Unit,
    onAddHealthNote: (HealthIssueType) -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit = {},
) {
    val selectedElder = uiState.eldersList[uiState.selectedIndex]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        LoginBackButton(onBack)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            Spacer(Modifier.height(30.dp))
            Text(
                "건강정보 등록하기",
                style = MediCareCallTheme.typography.B_26,
                color = MediCareCallTheme.colors.black,
            )
            Spacer(Modifier.height(20.dp))

            ElderSelector(
                elderNames = uiState.eldersList.map { it.nameState.text.toString() },
                selectedIndex = uiState.selectedIndex,
                onSelectElder = onSelectElder,
            )

            Spacer(Modifier.height(20.dp))
            DiseaseNamesItem(
                textState = uiState.diseaseInputText,
                diseaseList = selectedElder.diseases,
                onRemoveChip = onRemoveDisease,
                onAddDisease = onAddDisease,
            )
            Spacer(Modifier.height(20.dp))

            MedicationItem(
                medications = selectedElder.medications,
                inputTextState = uiState.medicationInputText,
                selectedTimes = uiState.selectedMedicationTimes.toList(),
                onRemoveMedication = onRemoveMedication,
                onSelectTime = onSelectTime,
                onAddMedication = onAddMedication,
            )

            Spacer(Modifier.height(20.dp))
            Text(
                "특이사항",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17,
            )
            Spacer(Modifier.height(10.dp))

            if (selectedElder.notes.isNotEmpty()) {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    selectedElder.notes.forEach { note ->
                        ChipItem(note.displayName) { onRemoveHealthNote(note) }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            DefaultDropdown(
                value = null,
                enumList = HealthIssueType.entries.toList(),
                placeHolder = "특이사항 선택하기",
                scrollState = scrollState,
                onOptionSelect = onAddHealthNote,
                displayText = { it.displayName },
            )
        }
        CTAButton(
            type = CTAButtonType.GREEN,
            text = "다음",
            onClick = onNextClick,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
        )
    }
}

@Composable
private fun ElderSelector(
    elderNames: List<String>,
    selectedIndex: Int,
    onSelectElder: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Row(modifier.horizontalScroll(scrollState)) {
        elderNames.forEachIndexed { index, name ->
            Box(
                Modifier
                    .clip(shape = CircleShape)
                    .background(
                        if (index == selectedIndex) MediCareCallTheme.colors.main else MediCareCallTheme.colors.white,
                    )
                    .border(
                        width = 1.2.dp,
                        color = if (index == selectedIndex) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                        shape = CircleShape,
                    )
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = { onSelectElder(index) },
                    ),
            ) {
                Text(
                    text = name,
                    style = if (index == selectedIndex) MediCareCallTheme.typography.SB_14 else MediCareCallTheme.typography.R_14,
                    color = if (index == selectedIndex) MediCareCallTheme.colors.white else MediCareCallTheme.colors.gray5,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun LoginElderMedInfoScreenPreview() {
    MediCareCallTheme {
        LoginElderMedInfoScreenLayout(
            uiState = LoginElderUiState(),
            scrollState = rememberScrollState(),
            onSelectElder = {},
            onRemoveDisease = {},
            onAddDisease = {},
            onRemoveMedication = {},
            onSelectTime = {},
            onAddMedication = {},
            onRemoveHealthNote = {},
            onAddHealthNote = {},
            onNextClick = {},
            onBack = {},
        )
    }
}
