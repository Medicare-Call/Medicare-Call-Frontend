package com.konkuk.medicarecall.ui.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.PhoneNumberVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.settings.component.DeleteConfirmDialog
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailElderInfoViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ElderDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderId: Int,
    navController: NavHostController,
    detailViewModel: DetailElderInfoViewModel = koinViewModel(),
) {
    // 뷰모델로부터 데이터 및 성공 여부 상태 수집
    val elderData by detailViewModel.uiState.collectAsStateWithLifecycle()
    val isSuccess by detailViewModel.isSuccess.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()


    var isMale by remember { mutableStateOf<Boolean?>(null) }
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf(RelationshipType.ACQUAINTANCE) }
    var residenceType by remember { mutableStateOf(ElderResidenceType.ALONE) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // 수정 모드일 때 ID로 데이터를 로드
    LaunchedEffect(elderId) {
        if (elderId != -1) {
            detailViewModel.loadElderDataById(elderId)
        }
    }

    // 수정 모드일 때 서버에서 불러온 데이터를 UI 필드에 동기화
    LaunchedEffect(elderData) {
        elderData?.let {
            name = it.name
            isMale = it.gender == GenderType.MALE
            phoneNum = it.phone
            relationship = it.relationship
            residenceType = it.residenceType

            // 날짜 형식 변환 (yyyy-MM-dd -> yyyyMMdd)
            try {
                val parseDate = LocalDate.parse(it.birthDate)
                birth = parseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            } catch (e: Exception) {
                birth = ""
            }
        }
    }

    // 작업 성공(등록/수정 완료) 시 화면 이동 처리
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("ELDER_NAME_UPDATED", name)
            onBack()
        }
    }

    // ID 값에 따른 모드 판별 및 텍스트 설정
    val isEditMode = elderId != -1
    val screenTitle = if (isEditMode) "어르신 개인정보 설정" else "어르신 등록"
    val confirmButtonText = if (isEditMode) "수정 완료" else "등록 완료"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        SettingsTopAppBar(
            title = screenTitle,
            leftIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "setting back",
                    modifier = modifier.clickable { onBack() },
                    tint = MediCareCallTheme.colors.black,
                )
            },
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(Modifier.height(20.dp))

            // 수정 모드일 때만 '삭제' 텍스트 노출
            if (isEditMode) {
                Row {
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        text = "삭제",
                        color = MediCareCallTheme.colors.negative,
                        style = MediCareCallTheme.typography.SB_16,
                        modifier = Modifier.clickable {
                            showDeleteDialog = true
                        },
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Column {
                    DefaultTextField(
                        value = name,
                        onValueChange = { name = it },
                        category = "이름",
                        placeHolder = "이름",
                    )
                }
                Column {
                    DefaultTextField(
                        value = birth,
                        onValueChange = { birth = it },
                        category = "생년월일",
                        placeHolder = "YYYY / MM / DD",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateOfBirthVisualTransformation(),
                        maxLength = 8,
                    )
                }
                Column {
                    Text(
                        "성별",
                        style = MediCareCallTheme.typography.M_17,
                        color = MediCareCallTheme.colors.gray7,
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    GenderToggleButton(
                        isMale = isMale ?: true, // 초기값 null 대응
                        onGenderChange = { newValue ->
                            isMale = newValue
                        },
                    )
                }
                Column {
                    DefaultTextField(
                        value = phoneNum,
                        onValueChange = { phoneNum = it },
                        placeHolder = "휴대폰 번호",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = PhoneNumberVisualTransformation(),
                        maxLength = 11,
                    )
                }
                Column {
                    DefaultDropdown(
                        enumList = RelationshipType.entries.map { it.displayName }
                            .toList(),
                        placeHolder = "관계 선택하기",
                        category = "어르신과의 관계",
                        scrollState,
                        value = relationship.displayName,
                        onOptionSelect = { newValue ->
                            relationship = RelationshipType.entries.firstOrNull {
                                it.displayName == newValue
                            } ?: RelationshipType.ACQUAINTANCE
                        },
                    )
                }
                Column {
                    DefaultDropdown(
                        enumList = ElderResidenceType.entries.map { it.displayName }
                            .toList(),
                        placeHolder = "거주방식을 선택해주세요",
                        category = "어르신 거주 방식",
                        scrollState,
                        value = residenceType.displayName,
                        onOptionSelect = { newValue ->
                            residenceType = ElderResidenceType.entries.firstOrNull {
                                it.displayName == newValue
                            } ?: ElderResidenceType.WITH_FAMILY
                        },
                    )
                }

                CTAButton(
                    type = if (
                        name.matches(Regex("^[가-힣a-zA-Z]*$")) &&
                        birth.length == 8 &&
                        birth.isValidDate() &&
                        phoneNum.length == 11 &&
                        phoneNum.startsWith("010")
                    ) {
                        CTAButtonType.GREEN
                    } else {
                        CTAButtonType.DISABLED
                    },
                    text = confirmButtonText,
                    onClick = {
                        // 통합 처리 함수(processElderInfo)를 사용하여 등록/수정 요청
                        val requestDto = ElderRegisterRequestDto(
                            name = name,
                            birthDate = toDashedDate(birth),
                            gender = if (isMale == true) GenderType.MALE else GenderType.FEMALE,
                            phone = phoneNum,
                            relationship = relationship,
                            residenceType = residenceType,
                        )
                        detailViewModel.processElderInfo(requestDto)
                    },
                    modifier = Modifier.padding(bottom = 20.dp),
                )
            }
        }
        if (showDeleteDialog) {
            DeleteConfirmDialog(
                onDismiss = { showDeleteDialog = false },
                onDelete = {
                    showDeleteDialog = false
                    detailViewModel.deleteElderInfo(elderId)
                    onBack()
                },
            )
        }
    }
}

// 날짜 형식 변환 함수 (기존 유지)
fun toDashedDate(yyyymmdd: String): String {
    val d = yyyymmdd.filter { it.isDigit() }
    if (d.length != 8) return yyyymmdd // 에러 방지 위해 8자리 아닐 시 그대로 반환
    return "${d.substring(0, 4)}-${d.substring(4, 6)}-${d.substring(6, 8)}"
}
