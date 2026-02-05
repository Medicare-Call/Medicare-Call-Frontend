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
    viewModel: LoginInfoViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.MemberRegisterSuccess -> {
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

                else -> {}
            }
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding()
            .imePadding(),
    ) {
        LoginMyInfoScreenLayout(
            name = uiState.userInfo.name,
            dateOfBirth = uiState.userInfo.birthDate,
            gender = uiState.userInfo.gender,
            onNameChanged = viewModel::onNameChanged,
            onDOBChanged = { input ->
                val filtered = input.filter { it.isDigit() }.take(8)
                viewModel.onDOBChanged(filtered)
            },
            onGenderChanged = viewModel::onGenderChanged,
            onNextClick = {
                if (!uiState.userInfo.name.matches(Regex("^[가-힣a-zA-Z]*$"))) {
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            "이름을 다시 확인해주세요",
                            duration = SnackbarDuration.Short,
                        )
                    }
                } else if (!uiState.userInfo.birthDate.isValidDate()) {
                    coroutineScope.launch {
                        snackBarState.showSnackbar(
                            "생년월일을 다시 확인해주세요",
                            duration = SnackbarDuration.Short,
                        )
                    }
                } else {
                    viewModel.setShowBottomSheet(true)
                }
            },
            onBack = onBack,
            focusRequester = focusRequester,
        )
        DefaultSnackBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            hostState = snackBarState,
        )
    }

    if (uiState.showBottomSheet) {
        val itemList = listOf(
            "서비스 이용약관",
            "개인정보 수집 및 이용 동의",
        )
        val isCheckedAll = uiState.checkedStates.all { it }

        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowBottomSheet(false) },
            sheetState = sheetState,
            containerColor = MediCareCallTheme.colors.bg,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        ) {
            Column {
                Text(
                    "회원가입을 위해\n약관 동의가 필요합니다",
                    color = MediCareCallTheme.colors.black,
                    style = MediCareCallTheme.typography.B_20,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = { viewModel.setAllAgreeCheckState(!uiState.allAgreeCheckState) },
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

            itemList.forEachIndexed { index, title ->
                AgreementItem(
                    title,
                    isChecked = uiState.checkedStates[index],
                    onCheckedChange = { viewModel.setCheckedState(index, !uiState.checkedStates[index]) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }

            CTAButton(
                type = if (isCheckedAll) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "다음",
                onClick = { viewModel.memberRegister() },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp, top = 20.dp),
            )
        }
    }
}

@Composable
private fun LoginMyInfoScreenLayout(
    modifier: Modifier = Modifier,
    name: String,
    dateOfBirth: String,
    gender: GenderType = GenderType.MALE,
    onNameChanged: (String) -> Unit,
    onDOBChanged: (String) -> Unit,
    onGenderChanged: (GenderType) -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit = {},
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        LoginBackButton(onBack)
        Column(
            modifier = Modifier.verticalScroll(scrollState),
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
                    onDOBChanged,
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
                GenderToggleButton(gender == GenderType.MALE) { isMale ->
                    onGenderChanged(if (isMale) GenderType.MALE else GenderType.FEMALE)
                }
            }
            Spacer(Modifier.height(30.dp))
            CTAButton(
                type = if (name.isNotEmpty() && dateOfBirth.length == 8) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "다음",
                onClick = onNextClick,
                modifier = Modifier.padding(bottom = 20.dp),
            )
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
            gender = GenderType.MALE,
            onNameChanged = {},
            onDOBChanged = {},
            onGenderChanged = {},
            onNextClick = {},
        )
    }
}
