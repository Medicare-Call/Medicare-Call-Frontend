package com.konkuk.medicarecall.ui.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val myDataInfo by detailMyDataViewModel.myDataInfo.collectAsStateWithLifecycle()
    val isLoading by detailMyDataViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by detailMyDataViewModel.errorMessage.collectAsStateWithLifecycle()

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

        when {
            isLoading && myDataInfo == null -> {
                // Loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("내 정보를 불러오는 중입니다...")
                }
            }

            errorMessage != null -> {
                // Error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = errorMessage ?: "오류가 발생했습니다",
                        color = MediCareCallTheme.colors.negative,
                        style = MediCareCallTheme.typography.M_17,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CTAButton(
                        type = CTAButtonType.GREEN,
                        text = "다시 시도",
                        onClick = {
                            detailMyDataViewModel.loadMyData()
                        },
                    )
                }
            }

            myDataInfo != null -> {
                // Data loaded state
                MyDetailContent(
                    myDataInfo = myDataInfo!!,
                    detailMyDataViewModel = detailMyDataViewModel,
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
private fun MyDetailContent(
    myDataInfo: MyInfoResponseDto,
    detailMyDataViewModel: DetailMyDataViewModel,
    onBack: () -> Unit,
) {
    var isMale by remember { mutableStateOf<Boolean>(myDataInfo.gender == GenderType.MALE) }
    var name by remember { mutableStateOf(myDataInfo.name) }
    var birth by remember { mutableStateOf(myDataInfo.birthDate.replace("-", "")) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(scrollState),
    ) {
        DefaultTextField(
            value = name,
            onValueChange = { name = it },
            category = "이름",
            placeHolder = "이름",
        )
        Spacer(modifier = Modifier.height(20.dp))
        DefaultTextField(
            value = birth,
            onValueChange = { birth = it },
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
                onGenderChange = { newValue ->
                    isMale = newValue
                },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        CTAButton(
            type = if (name.matches(Regex("^[가-힣a-zA-Z]*$")) &&
                birth.length == 8 &&
                birth.isValidDate() &&
                isMale != null
            ) CTAButtonType.GREEN else CTAButtonType.DISABLED,
            text = "확인",
            onClick = {
                val gender = if (isMale == true) GenderType.MALE else GenderType.FEMALE
                detailMyDataViewModel.updateUserData(
                    userInfo = MyInfoResponseDto(
                        name = name,
                        birthDate = birth.replaceFirst(
                            "(\\d{4})(\\d{2})(\\d{2})".toRegex(),
                            "$1-$2-$3",
                        ),
                        gender = gender,
                        phone = myDataInfo.phone,
                        pushNotification = myDataInfo.pushNotification,
                    ),
                ) { onBack() }
            },
        )
    }
}
