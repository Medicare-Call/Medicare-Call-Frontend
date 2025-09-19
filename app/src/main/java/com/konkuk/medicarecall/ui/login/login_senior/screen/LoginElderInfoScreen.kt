package com.konkuk.medicarecall.ui.login.login_senior.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.navigation.Route
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.login.login_senior.component.ElderInputForm
import com.konkuk.medicarecall.ui.login.login_info.component.LoginBackButton
import com.konkuk.medicarecall.ui.login.login_senior.LoginElderViewModel
import com.konkuk.medicarecall.ui.model.CTAButtonType
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.common.util.isValidDate
import kotlinx.coroutines.launch

@Composable
fun LoginElderScreen(
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
            .imePadding(),
    ) {
        Column {
            LoginBackButton({
                navController.navigate(Route.LoginElderInfoScreen.route) {
                    popUpTo(Route.LoginStart.route) { inclusive = false } // ← 스택 정리
                    launchSingleTop = true
                    restoreState = true
                }
            })
            Column(
                modifier
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "어르신 등록하기",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black
                )
                Spacer(Modifier.height(30.dp))
                repeat(loginElderViewModel.elders) { index ->
                    ElderInputForm(
                        loginElderViewModel,
                        scrollState,
                        index == loginElderViewModel.expandedFormIndex,
                        index
                    )
                }


                val interactionSource = remember { MutableInteractionSource() }
                // interactionSource 에서 pressed 상태 감지
                val isPressed by interactionSource.collectIsPressedAsState()

                // 어르신 더 추가하기 버튼
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (loginElderViewModel.isInputComplete()) {
                                if (isPressed)
                                    MediCareCallTheme.colors.g200
                                else
                                    MediCareCallTheme.colors.g50
                            } else MediCareCallTheme.colors.gray1
                        )
                        .border(
                            1.2.dp,
                            color = if (loginElderViewModel.isInputComplete())
                                MediCareCallTheme.colors.main
                            else
                                MediCareCallTheme.colors.gray3,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable(
                            enabled = loginElderViewModel.isInputComplete(),
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            if (loginElderViewModel.elders < 5) {
                                loginElderViewModel.elders++
                                loginElderViewModel.expandedFormIndex =
                                    loginElderViewModel.elders - 1
                                loginElderViewModel.onNameChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    ""
                                )
                                loginElderViewModel.onDOBChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    ""
                                )
                                loginElderViewModel.onRelationshipChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    ""
                                )
                                loginElderViewModel.onGenderChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    null
                                )
                                loginElderViewModel.onLivingTypeChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    ""
                                )
                                loginElderViewModel.onPhoneNumberChanged(
                                    loginElderViewModel.expandedFormIndex,
                                    ""
                                )


                            } else {
                                coroutineScope.launch {
                                    snackBarState.showSnackbar("어르신은 최대 5명까지 등록이 가능해요")

                                }
                            }
                        }
                ) {
                    Row(
                        Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.Center)

                    ) {
                        Icon(
                            painterResource(R.drawable.ic_plus), contentDescription = "플러스 아이콘",
                            tint = if (loginElderViewModel.isInputComplete())
                                MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.gray3
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "어르신 더 추가하기",
                            color = if (loginElderViewModel.isInputComplete())
                                MediCareCallTheme.colors.main
                            else MediCareCallTheme.colors.gray3,
                            style = MediCareCallTheme.typography.B_17
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))
                CTAButton(
                    if (loginElderViewModel.isInputComplete())
                        CTAButtonType.GREEN
                    else CTAButtonType.DISABLED,
                    "다음",
                    {
                        if (!loginElderViewModel.nameList.filter { it.isNotEmpty() }
                                .all {
                                    it.matches(Regex("^[가-힣a-zA-Z]*$"))
                                }
                        )
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "이름을 다시 확인해주세요",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        else if (!loginElderViewModel.dateOfBirthList.filter { it.isNotEmpty() }
                                .all {
                                    it.isValidDate()
                                })
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "생년월일을 다시 확인해주세요",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        else if (!loginElderViewModel.phoneNumberList.filter { it.isNotEmpty() }
                                .all { it.startsWith("010") })
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "휴대폰 번호를 다시 확인해주세요",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        else {
                            loginElderViewModel.createElderDataList()
                            navController.navigate(Route.LoginElderMedInfoScreen.route)
                        }
                    },
                    modifier.padding(bottom = 20.dp)
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
