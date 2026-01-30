package com.konkuk.medicarecall.ui.feature.login.myinfo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.login.myinfo.component.AgreementItem
import com.konkuk.medicarecall.ui.feature.login.myinfo.component.LoginBackButton
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginEvent
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.GenderType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginMyInfoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToRegisterElder: () -> Unit = {},
    loginInfoViewModel: LoginInfoViewModel,
) {
    val uiState by loginInfoViewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        loginInfoViewModel.events.collect { event ->
            when (event) {
                is LoginEvent.MemberRegisterSuccess -> {
                    // 인증 성공 시 어르신정보 화면으로 이동
                    navigateToRegisterElder()
                }

                is LoginEvent.MemberRegisterFailure -> {
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            message = "오류가 발생했습니다 다시 시도해주세요",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                else -> { /* 다른 이벤트 무시 */
                }
            }
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
            .imePadding(),
    ) {
        Column {
            LoginBackButton(onBack)
            Column(
                Modifier
                    .verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "회원 정보를\n입력해주세요",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(Modifier.height(40.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "이름",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    DefaultTextField(
                        uiState.name,
                        {
                            loginInfoViewModel.onNameChanged(it)
                        },
                        placeHolder = "이름",
                        textFieldModifier = Modifier.focusRequester(focusRequester),
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "생년월일",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    // 생년월일 입력 텍스트필드
                    DefaultTextField(
                        uiState.dateOfBirth,
                        { input ->
                            val filtered = input.filter { it.isDigit() }.take(8)
                            loginInfoViewModel.onDOBChanged(filtered)
                        },
                        placeHolder = "YYYY / MM / DD",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateOfBirthVisualTransformation(),
                        maxLength = 8,
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "성별",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )

                    GenderToggleButton(uiState.isMale) { loginInfoViewModel.onGenderChanged(it) }
                }
                Spacer(Modifier.height(30.dp))
                CTAButton(
                    if (uiState.name.isNotEmpty() &&
                        uiState.dateOfBirth.length == 8
                    ) CTAButtonType.GREEN
                    else
                        CTAButtonType.DISABLED,
                    "다음",
                    {
                        if (
                            !uiState.name.matches(Regex("^[가-힣a-zA-Z]*$"))
                        ) {
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "이름을 다시 확인해주세요",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        } else if (!uiState.dateOfBirth.isValidDate()) {
                            coroutineScope.launch {
                                snackBarState.showSnackbar(
                                    "생년월일을 다시 확인해주세요",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        } else {
                            loginInfoViewModel.setShowBottomSheet(true)
                        }
                    },
                    Modifier.padding(bottom = 20.dp),
                )

                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                )

                if (uiState.showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            loginInfoViewModel.setShowBottomSheet(false)
                        },
                        sheetState = sheetState,
                        containerColor = MediCareCallTheme.colors.bg,
                        dragHandle = null,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    ) {
                        // Sheet content

                        // 개별 약관 아이템 및 상태
                        val itemList = listOf(
                            "서비스 이용약관" to Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            "개인정보 수집 및 이용 동의" to Modifier.padding(
                                horizontal = 20.dp,
                                vertical = 8.dp,
                            ),
                        )
                        val isCheckedAll = uiState.checkedStates.all { it }

                        Column {
                            Text(
                                "회원가입을 위해\n약관 동의가 필요합니다",
                                color = MediCareCallTheme.colors.black,
                                style = MediCareCallTheme.typography.B_20,
                                modifier = modifier.padding(horizontal = 20.dp, vertical = 30.dp),
                            )
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                                    .clickable(
                                        interactionSource = null,
                                        indication = null,
                                        onClick = {
                                            loginInfoViewModel.setAllAgreeCheckState(!uiState.allAgreeCheckState)
                                        },
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_check_box),
                                    contentDescription = "체크박스",
                                    tint = if (uiState.allAgreeCheckState) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "전체 동의하기",
                                    color = MediCareCallTheme.colors.black,
                                    style = MediCareCallTheme.typography.SB_16,
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.4.dp,
                            color = MediCareCallTheme.colors.gray2,
                        )
                        Spacer(Modifier.height(12.dp))

                        itemList.forEachIndexed { index, (title, modifier) ->
                            AgreementItem(
                                title,
                                isChecked = uiState.checkedStates[index],
                                onCheckedChange = {
                                    loginInfoViewModel.setCheckedState(index, !uiState.checkedStates[index])
                                },
                                modifier = modifier,
                            )
                        }
                        // 모달 내부 CTA(다음) 버튼
                        CTAButton(
                            if (isCheckedAll) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                            "다음",
                            {
                                loginInfoViewModel.memberRegister(
                                    uiState.name,
                                    uiState.dateOfBirth,
                                    if (uiState.isMale) GenderType.MALE else GenderType.FEMALE,
                                )
                            },
                            modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 30.dp, top = 20.dp),
                        )
                    }
                }
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

@Composable
private fun LoginMyInfoScreenLayout(
    modifier: Modifier = Modifier,
    name: String,
    dateOfBirth: String,
    isMale: Boolean?,
    onNameChanged: (String) -> Unit,
    onDOBChanged: (String) -> Unit,
    onGenderChanged: (Boolean) -> Unit,
    onNextClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
            .imePadding(),
    ) {
        Column {
            Column(
                Modifier.verticalScroll(scrollState),
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "회원 정보를\n입력해주세요",
                    style = MediCareCallTheme.typography.B_26,
                    color = MediCareCallTheme.colors.black,
                )
                Spacer(Modifier.height(40.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "이름",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    DefaultTextField(
                        name,
                        onNameChanged,
                        placeHolder = "이름",
                        textFieldModifier = Modifier.focusRequester(focusRequester),
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "생년월일",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    DefaultTextField(
                        dateOfBirth,
                        { input ->
                            val filtered = input.filter { it.isDigit() }.take(8)
                            onDOBChanged(filtered)
                        },
                        placeHolder = "YYYY / MM / DD",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateOfBirthVisualTransformation(),
                        maxLength = 8,
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "성별",
                        color = MediCareCallTheme.colors.gray7,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    GenderToggleButton(isMale ?: true, onGenderChanged)
                }
                Spacer(Modifier.height(30.dp))
                CTAButton(
                    if (name.isNotEmpty() && dateOfBirth.length == 8 && isMale != null)
                        CTAButtonType.GREEN
                    else
                        CTAButtonType.DISABLED,
                    "다음",
                    onNextClick,
                    Modifier.padding(bottom = 20.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LoginMyInfoScreenPreview() {
    MediCareCallTheme {
        LoginMyInfoScreenLayout(
            name = "홍길동",
            dateOfBirth = "19900101",
            isMale = true,
            onNameChanged = {},
            onDOBChanged = {},
            onGenderChanged = {},
            onNextClick = {},
        )
    }
}
