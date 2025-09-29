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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.ui.navigation.Route
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.ChipItem
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DiseaseNamesItem
import com.konkuk.medicarecall.ui.common.component.MedicationItem
import com.konkuk.medicarecall.ui.feature.login.info.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.senior.LoginElderViewModel
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginElderMedInfoScreen(
    navController: NavController,
    loginElderViewModel: LoginElderViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(

        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .systemBarsPadding()
            .imePadding()
    ) {
        Column {
            LoginBackButton({
                navController.popBackStack()
            })
            Column(
                Modifier
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(30.dp))
                Text(
                    "건강정보 등록하기",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black
                )
                Spacer(Modifier.height(20.dp))

                val scrollState = rememberScrollState()
                // 상단 어르신 선택 Row
                Row(Modifier.horizontalScroll(scrollState)) {
                    loginElderViewModel.elderDataList.forEachIndexed { index, elder ->

                        Box(
                            Modifier
                                .clip(shape = CircleShape)
                                .background(
                                    if (index == loginElderViewModel.selectedElder)
                                        MediCareCallTheme.colors.main
                                    else MediCareCallTheme.colors.white
                                )
                                .border(
                                    width = 1.2.dp,
                                    color = if (index == loginElderViewModel.selectedElder)
                                        MediCareCallTheme.colors.main
                                    else MediCareCallTheme.colors.gray2,
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        loginElderViewModel.onSelectedElderChanged(index)

                                    }
                                )

                        ) {
                            Text(
                                text = elder.name,
                                style = if (index == loginElderViewModel.selectedElder)
                                    MediCareCallTheme.typography.SB_14
                                else MediCareCallTheme.typography.R_14,
                                color = if (index == loginElderViewModel.selectedElder)
                                    MediCareCallTheme.colors.white
                                else MediCareCallTheme.colors.gray5,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp)) // 버튼 간격
                    }
                }
                Spacer(Modifier.height(20.dp))
                DiseaseNamesItem(
                    loginElderViewModel.diseaseInputText[loginElderViewModel.selectedElder],
                    loginElderViewModel.diseaseList[loginElderViewModel.selectedElder]
                )
                Spacer(Modifier.height(20.dp))

                MedicationItem(
                    loginElderViewModel.medMap[loginElderViewModel.selectedElder],
                    loginElderViewModel.medInputText[loginElderViewModel.selectedElder],
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    "특이사항",
                    color = MediCareCallTheme.colors.gray7,
                    style = MediCareCallTheme.typography.M_17
                )
                Spacer(Modifier.height(10.dp))

                if (loginElderViewModel.healthIssueList[loginElderViewModel.selectedElder].isNotEmpty()) {
                    Row(
                        Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        loginElderViewModel.healthIssueList[loginElderViewModel.selectedElder].forEach { healthIssue ->
                            ChipItem(healthIssue) {
                                loginElderViewModel.healthIssueList[loginElderViewModel.selectedElder].remove(
                                    healthIssue
                                )
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
                        if (it !in loginElderViewModel.healthIssueList[loginElderViewModel.selectedElder])
                            loginElderViewModel.healthIssueList[loginElderViewModel.selectedElder].add(
                                it
                            )
                    }
                )
                CTAButton(
                    CTAButtonType.GREEN,
                    "다음",
                    {
                        coroutineScope.launch {
                            loginElderViewModel.createElderHealthDataList()
                            loginElderViewModel.updateAllElders()
                            loginElderViewModel.updateAllEldersHealthInfo()
                            loginElderViewModel.postElderAndHealth()
                            delay(200L)
                            navController.navigate(Route.LoginCareCallSetting.route) {
                                popUpTo(Route.LoginRegisterElder.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    Modifier.padding(top = 30.dp, bottom = 20.dp)
                )
            }
        }
        DefaultSnackBar(
            snackBarState,
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp)
        )
    }
}
