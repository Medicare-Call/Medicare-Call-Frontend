package com.konkuk.medicarecall.ui.feature.login.senior.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.ChipItem
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DiseaseNamesItem
import com.konkuk.medicarecall.ui.common.component.MedicationItem
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.senior.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import kotlinx.coroutines.launch

@Composable
fun LoginElderMedInfoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToCareCallSetting: () -> Unit = {},
    loginElderViewModel: LoginElderViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()

    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val elderUiState by loginElderViewModel.elderUiState.collectAsState()

    val uiState by loginElderViewModel.elderHealthUiState.collectAsState()
    val selectedIndex = uiState.selectedIndex

    Box(

        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .systemBarsPadding()
            .imePadding(),
    ) {
        Column {
            LoginBackButton(onBack)
            Column(
                Modifier
                    .verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(30.dp))
                Text(
                    "건강정보 등록하기",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(Modifier.height(20.dp))

                val scrollState = rememberScrollState()
                // 상단 어르신 선택 Row
                Row(Modifier.horizontalScroll(scrollState)) {
                    elderUiState.eldersList.forEachIndexed { index, elder ->
                        Box(
                            Modifier
                                .clip(shape = CircleShape)
                                .background(
                                    if (index == selectedIndex)
                                        MediCareCallTheme.colors.main
                                    else MediCareCallTheme.colors.white,
                                )
                                .border(
                                    width = 1.2.dp,
                                    color = if (index == selectedIndex)
                                        MediCareCallTheme.colors.main
                                    else MediCareCallTheme.colors.gray2,
                                    shape = CircleShape,
                                )
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        loginElderViewModel.selectElderInHealth(index)
                                    },
                                ),
                        ) {
                            Text(
                                text = elder.name,
                                style = if (index == selectedIndex)
                                    MediCareCallTheme.typography.SB_14
                                else MediCareCallTheme.typography.R_14,
                                color = if (index == selectedIndex)
                                    MediCareCallTheme.colors.white
                                else MediCareCallTheme.colors.gray5,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp)) // 버튼 간격
                    }
                }
                Spacer(Modifier.height(20.dp))
                DiseaseNamesItem(
                    inputText = uiState.diseaseInputText,
                    diseaseList = uiState.elderHealthList[selectedIndex].diseaseNames,
                    onTextChanged = {
                        loginElderViewModel.updateDiseasesText(it)
                    },
                    onRemoveChip = {
                        loginElderViewModel.removeDisease(it)
                    },
                    onAddDisease = { loginElderViewModel.addDisease(it) },
                )
                Spacer(Modifier.height(20.dp))

                MedicationItem(
                    medicationSchedule = uiState.elderHealthList[selectedIndex].medicationMap,
                    inputText = uiState.medicationInputText,
                    selectedList = uiState.selectedMedicationTimes,
                    onTextChange = { loginElderViewModel.updateMedicationText(it) },
                    onRemoveChip = { time, medicine -> loginElderViewModel.removeMedication(time, medicine) },
                    onSelectTime = { loginElderViewModel.selectMedicationTime(it) },
                    onAddMedication = { time, medicine -> loginElderViewModel.addMedication(time, medicine) },
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    "특이사항",
                    color = MediCareCallTheme.colors.gray7,
                    style = MediCareCallTheme.typography.M_17,
                )
                Spacer(Modifier.height(10.dp))

                if (uiState.elderHealthList[selectedIndex].notes.isNotEmpty()) {
                    Row(
                        Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        uiState.elderHealthList[selectedIndex].notes.forEach { note ->
                            ChipItem(note) {
                                loginElderViewModel.removeHealthNote(note)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                DefaultDropdown(
                    HealthIssueType.entries.map { it.displayName }.toList(),
                    "특이사항 선택하기",
                    null,
                    scrollState,
                    {
                        loginElderViewModel.addHealthNote(it)
                    },
                )
                CTAButton(
                    CTAButtonType.GREEN,
                    "다음",
                    {
                        coroutineScope.launch {
                            loginElderViewModel.postElderHealthInfoBulk()
                            navigateToCareCallSetting()
                        }
                    },
                    Modifier.padding(top = 30.dp, bottom = 20.dp),
                )
            }
        }
        DefaultSnackBar(
            snackBarState,
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
        )
    }
}
