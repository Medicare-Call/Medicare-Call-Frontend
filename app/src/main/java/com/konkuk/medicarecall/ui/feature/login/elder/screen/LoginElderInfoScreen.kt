package com.konkuk.medicarecall.ui.feature.login.elder.screen

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultSnackBar
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.login.elder.component.ElderInputForm
import com.konkuk.medicarecall.ui.feature.login.elder.component.ElderRow
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.feature.login.myinfo.component.LoginBackButton
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginElderScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigateToRegisterElderHealth: () -> Unit = {},
    viewModel: LoginElderViewModel = koinViewModel(),
) {
    val uiState by viewModel.loginElderUiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedIndex) {
        delay(100L)
        scrollState.animateScrollTo(0)
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        LoginElderScreenLayout(
            eldersList = uiState.eldersList,
            selectedIndex = uiState.selectedIndex,
            isInputComplete = viewModel.isInputComplete(),
            scrollState = scrollState,
            onBack = onBack,
            onGenderChanged = viewModel::updateElderGender,
            onRelationshipChange = viewModel::updateElderRelationship,
            onLivingTypeChanged = viewModel::updateElderLivingType,
            onAddElder = {
                if (uiState.eldersList.size < 5) {
                    viewModel.addElder()
                } else {
                    coroutineScope.launch {
                        snackBarState.showSnackbar("어르신은 최대 5명까지 등록이 가능해요")
                    }
                }
            },
            onRemoveElder = viewModel::removeElder,
            onSelectElder = viewModel::setSelectedIndex,
            onNextClick = {
                val eldersList = uiState.eldersList
                when {
                    !eldersList.filter { it.nameState.text.isNotEmpty() }
                        .all { it.nameState.text.toString().matches(Regex("^[가-힣a-zA-Z]*$")) } -> {
                        coroutineScope.launch {
                            snackBarState.showSnackbar(
                                "이름을 다시 확인해주세요",
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }

                    !eldersList.filter { it.birthDateState.text.isNotEmpty() }
                        .all { it.birthDateState.text.toString().isValidDate() } -> {
                        coroutineScope.launch {
                            snackBarState.showSnackbar(
                                "생년월일을 다시 확인해주세요",
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }

                    !eldersList.filter { it.phoneNumberState.text.isNotEmpty() }
                        .all { it.phoneNumberState.text.toString().startsWith("010") } -> {
                        coroutineScope.launch {
                            snackBarState.showSnackbar(
                                "휴대폰 번호를 다시 확인해주세요",
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }

                    else -> {
                        viewModel.postElderBulk()
                        viewModel.setSelectedIndex(0) // 선택된 어르신 인덱스 초기화
                        navigateToRegisterElderHealth()
                    }
                }
            },
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
private fun LoginElderScreenLayout(
    modifier: Modifier = Modifier,
    eldersList: List<LoginElderData>,
    selectedIndex: Int,
    isInputComplete: Boolean,
    scrollState: ScrollState,
    onBack: () -> Unit,
    onGenderChanged: (GenderType) -> Unit,
    onRelationshipChange: (Relationship) -> Unit,
    onLivingTypeChanged: (ElderResidence) -> Unit,
    onAddElder: () -> Unit,
    onRemoveElder: (Int) -> Unit,
    onSelectElder: (Int) -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        LoginBackButton(onClick = onBack)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                "어르신 등록하기",
                style = MediCareCallTheme.typography.B_26,
                color = MediCareCallTheme.colors.black,
            )
            if (eldersList.size != 1) {
                Spacer(Modifier.height(30.dp))
                ElderRow(
                    selectedIndex = selectedIndex,
                    eldersList = eldersList,
                    onRemoveElder = onRemoveElder,
                    onSelectElder = onSelectElder,
                )
            }
            Spacer(Modifier.height(30.dp))
            ElderInputForm(
                scrollState = scrollState,
                elderData = eldersList[selectedIndex],
                onGenderChanged = onGenderChanged,
                onRelationshipChange = onRelationshipChange,
                onLivingTypeChanged = onLivingTypeChanged,
            )

            AddElderButton(
                isInputComplete = isInputComplete,
                onClick = onAddElder,
            )
            Spacer(Modifier.height(30.dp))
        }
        CTAButton(
            type = if (isInputComplete) CTAButtonType.GREEN else CTAButtonType.DISABLED,
            text = "다음",
            onClick = onNextClick,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
        )
    }
}

@Composable
private fun AddElderButton(
    isInputComplete: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isInputComplete) {
                    if (isPressed) MediCareCallTheme.colors.g200 else MediCareCallTheme.colors.g50
                } else {
                    MediCareCallTheme.colors.gray1
                },
            )
            .border(
                1.2.dp,
                color = if (isInputComplete) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(
                enabled = isInputComplete,
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick,
            ),
    ) {
        Row(
            Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        ) {
            Icon(
                painterResource(R.drawable.ic_plus),
                contentDescription = "플러스 아이콘",
                tint = if (isInputComplete) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "어르신 더 추가하기",
                color = if (isInputComplete) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray3,
                style = MediCareCallTheme.typography.B_17,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun LoginElderScreenPreview() {
    MediCareCallTheme {
        LoginElderScreenLayout(
            eldersList = listOf(
                LoginElderData(
                    nameState = TextFieldState("김옥자"),
                    birthDateState = TextFieldState("19500101"),
                    gender = GenderType.FEMALE,
                    phoneNumberState = TextFieldState("01012345678"),
                    relationship = Relationship.CHILD,
                    livingType = ElderResidence.WITH_FAMILY,
                ),
                LoginElderData(
                    nameState = TextFieldState("박막례"),
                    birthDateState = TextFieldState("19480315"),
                    gender = GenderType.FEMALE,
                    phoneNumberState = TextFieldState("01087654321"),
                    relationship = Relationship.CHILD,
                    livingType = ElderResidence.ALONE,
                ),
            ),
            selectedIndex = 0,
            isInputComplete = true,
            scrollState = rememberScrollState(),
            onBack = {},
            onGenderChanged = {},
            onRelationshipChange = {},
            onLivingTypeChanged = {},
            onAddElder = {},
            onRemoveElder = {},
            onSelectElder = {},
            onNextClick = {},
        )
    }
}
