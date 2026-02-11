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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.common.component.DefaultDropdown
import com.konkuk.medicarecall.ui.common.component.DefaultTextField
import com.konkuk.medicarecall.ui.common.component.GenderToggleButton
import com.konkuk.medicarecall.ui.common.util.DateOfBirthVisualTransformation
import com.konkuk.medicarecall.ui.common.util.PhoneNumberVisualTransformation
import com.konkuk.medicarecall.ui.common.util.isValidDate
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.component.DeleteConfirmDialog
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel.SettingsElderInfoDetailViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsElderInfoDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    elderId: Long,
    navController: NavHostController,
    viewModel: SettingsElderInfoDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val isEditMode = elderId != -1L
    val screenTitle = if (isEditMode) "어르신 개인정보 설정" else "어르신 등록"
    val confirmButtonText = if (isEditMode) "수정 완료" else "등록 완료"

    LaunchedEffect(elderId) {
        if (isEditMode) {
            viewModel.loadElderDataById(elderId)
        }
    }

    LaunchedEffect(uiState.elderData) {
        uiState.elderData?.let {
            viewModel.initializeForm(it)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("ELDER_NAME_UPDATED", uiState.name)
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
            isEditMode && (uiState.isLoading || uiState.elderData == null) -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(scrollState),
                ) {
                    Spacer(Modifier.height(20.dp))

                    if (isEditMode) {
                        Row {
                            Spacer(modifier = modifier.weight(1f))
                            Text(
                                text = "삭제",
                                color = MediCareCallTheme.colors.negative,
                                style = MediCareCallTheme.typography.SB_16,
                                modifier = Modifier.clickable {
                                    viewModel.setShowDeleteDialog(true)
                                },
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        Column {
                            DefaultTextField(
                                value = uiState.name,
                                onValueChange = { viewModel.updateName(it) },
                                category = "이름",
                                placeHolder = "이름",
                            )
                        }
                        Column {
                            DefaultTextField(
                                value = uiState.birth,
                                onValueChange = { viewModel.updateBirth(it) },
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
                                isMale = uiState.isMale,
                                onGenderChange = { viewModel.updateIsMale(it) },
                            )
                        }
                        Column {
                            DefaultTextField(
                                value = uiState.phoneNum,
                                onValueChange = { viewModel.updatePhoneNum(it) },
                                placeHolder = "휴대폰 번호",
                                keyboardType = KeyboardType.Number,
                                visualTransformation = PhoneNumberVisualTransformation(),
                                maxLength = 11,
                            )
                        }
                        Column {
                            DefaultDropdown(
                                enumList = Relationship.entries.map { it.displayName }.toList(),
                                placeHolder = "관계 선택하기",
                                category = "어르신과의 관계",
                                scrollState,
                                value = uiState.relationship.displayName,
                                onOptionSelect = { newValue ->
                                    val rel = Relationship.entries.firstOrNull {
                                        it.displayName == newValue
                                    } ?: Relationship.ACQUAINTANCE
                                    viewModel.updateRelationship(rel)
                                },
                            )
                        }
                        Column {
                            DefaultDropdown(
                                enumList = ElderResidence.entries.map { it.displayName }.toList(),
                                placeHolder = "거주방식을 선택해주세요",
                                category = "어르신 거주 방식",
                                scrollState,
                                value = uiState.residenceType.displayName,
                                onOptionSelect = { newValue ->
                                    val res = ElderResidence.entries.firstOrNull {
                                        it.displayName == newValue
                                    } ?: ElderResidence.WITH_FAMILY
                                    viewModel.updateResidenceType(res)
                                },
                            )
                        }

                        CTAButton(
                            type = if (
                                uiState.name.isNotEmpty() &&
                                uiState.name.matches(Regex("^[가-힣a-zA-Z]+$")) &&
                                uiState.birth.length == 8 &&
                                uiState.birth.isValidDate() &&
                                uiState.phoneNum.length == 11 &&
                                uiState.phoneNum.startsWith("010")
                            ) {
                                CTAButtonType.GREEN
                            } else {
                                CTAButtonType.DISABLED
                            },
                            text = confirmButtonText,
                            onClick = {
                                viewModel.processElderInfo(
                                    elderId = elderId,
                                    name = uiState.name,
                                    birthDate = toDashedDate(uiState.birth),
                                    gender = if (uiState.isMale) GenderType.MALE else GenderType.FEMALE,
                                    phone = uiState.phoneNum,
                                    relationship = uiState.relationship,
                                    residenceType = uiState.residenceType,
                                )
                            },
                            modifier = Modifier.padding(bottom = 20.dp),
                        )
                    }
                }
            }
        }

        if (uiState.showDeleteDialog) {
            DeleteConfirmDialog(
                onDismiss = { viewModel.setShowDeleteDialog(false) },
                onDelete = {
                    viewModel.setShowDeleteDialog(false)
                    viewModel.deleteElderInfo(elderId)
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
