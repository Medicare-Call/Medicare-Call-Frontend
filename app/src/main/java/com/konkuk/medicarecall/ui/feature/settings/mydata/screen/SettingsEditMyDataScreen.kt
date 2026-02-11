package com.konkuk.medicarecall.ui.feature.settings.mydata.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel.SettingsEditMyDataViewModel
import com.konkuk.medicarecall.domain.model.UserInfo
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.domain.model.type.GenderType
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsEditMyDataScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: SettingsEditMyDataViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMyInfo()
    }

    LaunchedEffect(uiState.myDataInfo) {
        uiState.myDataInfo?.let { viewModel.initializeFormData(it) }
    }

    SettingsEditMyDataLayout(
        modifier = modifier,
        isLoading = uiState.isLoading && uiState.myDataInfo == null,
        name = uiState.name,
        birth = uiState.birth,
        isMale = uiState.isMale,
        isSubmitEnabled = uiState.name.matches(Regex("^[가-힣a-zA-Z]*$")) &&
            uiState.birth.length == 8 &&
            uiState.birth.isValidDate() &&
            uiState.myDataInfo != null,
        onBackClick = onBack,
        onNameChange = { viewModel.updateName(it) },
        onBirthChange = { viewModel.updateBirth(it) },
        onGenderChange = { viewModel.updateIsMale(it) },
        onSubmitClick = {
            uiState.myDataInfo?.let { info ->
                val gender = if (uiState.isMale) GenderType.MALE else GenderType.FEMALE
                viewModel.updateUserData(
                    userInfo = UserInfo(
                        name = uiState.name,
                        birthDate = uiState.birth.replaceFirst(
                            "(\\d{4})(\\d{2})(\\d{2})".toRegex(),
                            "$1-$2-$3",
                        ),
                        gender = gender,
                        phoneNumber = info.phoneNumber,
                        pushNotification = info.pushNotification,
                    ),
                ) { onBack() }
            }
        },
    )
}

@Composable
private fun SettingsEditMyDataLayout(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    name: String,
    birth: String,
    isMale: Boolean,
    isSubmitEnabled: Boolean,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onBirthChange: (String) -> Unit,
    onGenderChange: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.bg),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .statusBarsPadding(),
    ) {
        SettingsTopAppBar(
            title = "내 정보 설정",
            leftIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "setting back",
                    modifier = Modifier.clickable { onBackClick() },
                    tint = MediCareCallTheme.colors.black,
                )
            },
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            DefaultTextField(
                value = name,
                onValueChange = onNameChange,
                category = "이름",
                placeHolder = "이름",
            )
            Spacer(modifier = Modifier.height(20.dp))
            DefaultTextField(
                value = birth,
                onValueChange = onBirthChange,
                category = "생년월일",
                placeHolder = "YYYY / MM / DD",
                keyboardType = KeyboardType.Number,
                visualTransformation = DateOfBirthVisualTransformation(),
                maxLength = 8,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column {
                Text(
                    "성별",
                    style = MediCareCallTheme.typography.M_17,
                    color = MediCareCallTheme.colors.gray7,
                )
                Spacer(modifier = Modifier.height(10.dp))
                GenderToggleButton(
                    isMale = isMale,
                    onGenderChange = onGenderChange,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            CTAButton(
                type = if (isSubmitEnabled) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "확인",
                onClick = onSubmitClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsEditMyDataLayoutPreview() {
    MediCareCallTheme {
        SettingsEditMyDataLayout(
            isLoading = false,
            name = "홍길동",
            birth = "19900101",
            isMale = true,
            isSubmitEnabled = true,
            onBackClick = {},
            onNameChange = {},
            onBirthChange = {},
            onGenderChange = {},
            onSubmitClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsEditMyDataLayoutEmptyPreview() {
    MediCareCallTheme {
        SettingsEditMyDataLayout(
            isLoading = false,
            name = "",
            birth = "",
            isMale = false,
            isSubmitEnabled = false,
            onBackClick = {},
            onNameChange = {},
            onBirthChange = {},
            onGenderChange = {},
            onSubmitClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsEditMyDataLayoutLoadingPreview() {
    MediCareCallTheme {
        SettingsEditMyDataLayout(
            isLoading = true,
            name = "",
            birth = "",
            isMale = true,
            isSubmitEnabled = false,
            onBackClick = {},
            onNameChange = {},
            onBirthChange = {},
            onGenderChange = {},
            onSubmitClick = {},
        )
    }
}
