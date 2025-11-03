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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.R
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ElderDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    eldersInfoResponseDto: EldersInfoResponseDto,
    navController: NavHostController,
    detailViewModel: DetailElderInfoViewModel = hiltViewModel(),
) {
    val gender = when (eldersInfoResponseDto.gender) {
        GenderType.MALE -> true
        else -> false
    }
    val parseDate =
        LocalDate.parse(eldersInfoResponseDto.birthDate) // yyyy-MM-dd 형식의 문자열을 LocalDate로 변환
    val date = parseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val scrollState = rememberScrollState()

    var isMale by remember { mutableStateOf<Boolean>(gender) }
    var name by remember { mutableStateOf(eldersInfoResponseDto.name) }
    var birth by remember { mutableStateOf(date) }
    var phoneNum by remember { mutableStateOf(eldersInfoResponseDto.phone) }
    var relationship by remember { mutableStateOf(eldersInfoResponseDto.relationship) }
    var residenceType by remember { mutableStateOf(eldersInfoResponseDto.residenceType) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        SettingsTopAppBar(
            title = "어르신 개인정보 설정",
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
                        isMale = isMale,
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

//                Button(
//                    modifier = modifier.fillMaxWidth().height(50.dp),
//                    shape = RoundedCornerShape(14.dp),
//                    onClick = {},
//                    colors = ButtonDefaults.buttonColors(
//                        contentColor = MediCareCallTheme.colors.white,
//                        containerColor = MediCareCallTheme.colors.main
//                    )
//
//                ) {
//                    Text("확인")
//                }

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
                    text = "확인",
                    onClick = {
                        detailViewModel.updateElderInfo(
                            elderInfo = EldersInfoResponseDto(
                                elderId = eldersInfoResponseDto.elderId,
                                name = name,
                                birthDate = toDashedDate(birth),
                                gender = if (isMale == true) GenderType.MALE else GenderType.FEMALE,
                                phone = phoneNum,
                                relationship = relationship,
                                residenceType = residenceType,
                            ),
                        ) {
                            navController.getBackStackEntry("main")
                                .savedStateHandle["ELDER_NAME_UPDATED"] = name
                            navController.popBackStack()
                        }
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
                    detailViewModel.deleteElderInfo(eldersInfoResponseDto.elderId)
                    onBack() // 삭제 후 설정 화면으로 이동
                },
            )
        }
    }
}

fun toDashedDate(yyyymmdd: String): String {
    val d = yyyymmdd.filter { it.isDigit() }
    require(d.length == 8) { "yyyyMMdd 형식(8자리)이어야 합니다." }
    return "${d.substring(0, 4)}-${d.substring(4, 6)}-${d.substring(6, 8)}"
}
