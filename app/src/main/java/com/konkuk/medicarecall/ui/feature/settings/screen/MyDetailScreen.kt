package com.konkuk.medicarecall.ui.feature.settings.screen

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
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailMyDataViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.GenderType
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    detailMyDataViewModel: DetailMyDataViewModel = koinViewModel(),
) {
    // ViewModel 상태 구독
    val myDataInfo by detailMyDataViewModel.myDataInfo.collectAsStateWithLifecycle()
    val isLoading by detailMyDataViewModel.isLoading.collectAsStateWithLifecycle()
    val isMale by detailMyDataViewModel.isMale.collectAsStateWithLifecycle()
    val name by detailMyDataViewModel.name.collectAsStateWithLifecycle()
    val birth by detailMyDataViewModel.birth.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // 초기 데이터 로드
    LaunchedEffect(Unit) {
        detailMyDataViewModel.loadMyInfo()
    }

    // myDataInfo가 로드되면 폼 데이터 초기화
    LaunchedEffect(myDataInfo) {
        myDataInfo?.let { detailMyDataViewModel.initializeFormData(it) }
    }

    if (isLoading && myDataInfo == null) {
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
                    modifier = Modifier.clickable { onBack() },
                    tint = MediCareCallTheme.colors.black,
                )
            },
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(scrollState),
        ) {
            DefaultTextField(
                value = name,
                onValueChange = { detailMyDataViewModel.updateName(it) },
                category = "이름",
                placeHolder = "이름",
            )
            Spacer(modifier = modifier.height(20.dp))
            DefaultTextField(
                value = birth,
                onValueChange = { detailMyDataViewModel.updateBirth(it) },
                category = "생년월일",
                placeHolder = "YYYY / MM / DD",
                keyboardType = KeyboardType.Number,
                visualTransformation = DateOfBirthVisualTransformation(),
                maxLength = 8,
            )
            Spacer(modifier = modifier.height(20.dp))
            Column {
                Text(
                    "성별",
                    style = MediCareCallTheme.typography.M_17,
                    color = MediCareCallTheme.colors.gray7,
                )
                Spacer(modifier = modifier.height(10.dp))
                GenderToggleButton(
                    isMale = isMale,
                    onGenderChange = { newValue ->
                        detailMyDataViewModel.updateIsMale(newValue)
                    },
                )
            }
            Spacer(modifier = modifier.height(30.dp))
            CTAButton(
                type = if (name.matches(Regex("^[가-힣a-zA-Z]*$")) &&
                    birth.length == 8 &&
                    birth.isValidDate() &&
                    isMale != null &&
                    myDataInfo != null
                ) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                text = "확인",
                onClick = {
                    myDataInfo?.let { info ->
                        val gender = if (isMale == true) GenderType.MALE else GenderType.FEMALE
                        detailMyDataViewModel.updateUserData(
                            userInfo = MyInfoResponseDto(
                                name = name,
                                birthDate = birth.replaceFirst(
                                    "(\\d{4})(\\d{2})(\\d{2})".toRegex(),
                                    "$1-$2-$3",
                                ),
                                gender = gender,
                                phone = info.phone,
                                pushNotification = info.pushNotification,
                            ),
                        ) { onBack() }
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyDetailScreenPreview() {
    MediCareCallTheme {
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
                        tint = MediCareCallTheme.colors.black,
                    )
                },
            )
        }
    }
}
