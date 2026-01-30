package com.konkuk.medicarecall.ui.feature.settings.elderinfo.screen

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
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.PhoneNumberVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.component.DeleteConfirmDialog
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel.SettingsElderInfoDetailViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SettingsElderInfoDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderId: Int, // -1이면 등록, 그 외에는 수정
    navController: NavHostController,
    detailViewModel: SettingsElderInfoDetailViewModel = koinViewModel(),
) {
    // ViewModel 상태 구독
    val elderData by detailViewModel.uiState.collectAsStateWithLifecycle()
    val isSuccess by detailViewModel.isSuccess.collectAsStateWithLifecycle()
    val isLoading by detailViewModel.isLoading.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    // Local State: 폼 입력값 관리
    var isMale by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") } // yyyyMMdd (8자리)
    var phoneNum by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf(RelationshipType.ACQUAINTANCE) }
    var residenceType by remember { mutableStateOf(ElderResidenceType.ALONE) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ID 값에 따른 모드 판별
    val isEditMode = elderId != -1
    val screenTitle = if (isEditMode) "어르신 개인정보 설정" else "어르신 등록"
    val confirmButtonText = if (isEditMode) "수정 완료" else "등록 완료"

    // 초기 데이터 로드 (수정 모드일 때)
    LaunchedEffect(elderId) {
        if (isEditMode) {
            detailViewModel.loadElderDataById(elderId)
        }
    }

    // 서버 데이터 -> UI 필드 동기화
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
                // 파싱 실패 시 기존 값 유지하거나 빈 값 처리
            }
        }
    }

    // 작업 성공(등록/수정/삭제) 시 네비게이션 처리
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            // 변경된 이름을 이전 화면으로 전달 (Toast 메시지 등을 위해)
            navController.previousBackStackEntry?.savedStateHandle?.set("ELDER_NAME_UPDATED", name)
            onBack()
        }
    }

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

        when {
            // 수정 모드인데 데이터가 로딩 중이거나 아직 없을 때
            isEditMode && (isLoading || elderData == null) -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                // 폼 UI
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(scrollState),
                ) {
                    Spacer(Modifier.height(20.dp))

                    // 삭제 버튼 (수정 모드일 때만 표시)
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
                        // 이름
                        Column {
                            DefaultTextField(
                                value = name,
                                onValueChange = { name = it },
                                category = "이름",
                                placeHolder = "이름",
                            )
                        }
                        // 생년월일
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
                        // 성별
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
                                    isMale = newValue
                                },
                            )
                        }
                        // 전화번호
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
                        // 관계
                        Column {
                            DefaultDropdown(
                                enumList = RelationshipType.entries.map { it.displayName }.toList(),
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
                        // 거주 방식
                        Column {
                            DefaultDropdown(
                                enumList = ElderResidenceType.entries.map { it.displayName }.toList(),
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

                        // 확인/등록 버튼
                        CTAButton(
                            type = if (
                                name.isNotEmpty() &&
                                name.matches(Regex("^[가-힣a-zA-Z]+$")) &&
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
                                val requestDto = ElderRegisterRequestDto(
                                    name = name,
                                    birthDate = toDashedDate(birth), // yyyy-MM-dd 변환
                                    gender = if (isMale) GenderType.MALE else GenderType.FEMALE,
                                    phone = phoneNum,
                                    relationship = relationship,
                                    residenceType = residenceType,
                                )
                                // ViewModel의 통합 처리 함수 호출
                                detailViewModel.processElderInfo(elderId, requestDto)
                            },
                            modifier = Modifier.padding(bottom = 20.dp),
                        )
                    }
                }
            }
        }

        // 삭제 확인 다이얼로그
        if (showDeleteDialog) {
            DeleteConfirmDialog(
                onDismiss = { showDeleteDialog = false },
                onDelete = {
                    showDeleteDialog = false
                    detailViewModel.deleteElderInfo(elderId)
                    // 삭제 성공 시의 네비게이션은 isSuccess LaunchedEffect에서 처리됨
                },
            )
        }
    }
}

// 날짜 형식 변환 함수 (yyyyMMdd -> yyyy-MM-dd)
fun toDashedDate(yyyymmdd: String): String {
    val d = yyyymmdd.filter { it.isDigit() }
    if (d.length != 8) return yyyymmdd // 안전 장치
    return "${d.substring(0, 4)}-${d.substring(4, 6)}-${d.substring(6, 8)}"
}
