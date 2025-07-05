package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.login_info.uistate.LoginUiState
import com.konkuk.medicarecall.ui.login_info.component.AgreementItem
import com.konkuk.medicarecall.ui.component.CTAButton
import com.konkuk.medicarecall.ui.component.DefaultTextField
import com.konkuk.medicarecall.ui.component.GenderToggleButton
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.model.CTAButtonType
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.util.DateOfBirthVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginMyInfoScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        TopBar({
            loginViewModel.updateLoginUiState(LoginUiState.EnterVerificationCode)
            navController.popBackStack()
        })
        Spacer(Modifier.height(20.dp))
        Text(
            "회원 정보를\n입력해주세요",
            style = MediCareCallTheme.typography.B_26,
            color = MediCareCallTheme.colors.black
        )
        Spacer(Modifier.height(40.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "이름",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )
            DefaultTextField(
                loginViewModel.name,
                {
                    loginViewModel.onNameChanged(it)
                },
                "이름"
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "생년월일",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )
            // 생년월일 입력 텍스트필드
            DefaultTextField(
                loginViewModel.dateOfBirth,
                { input ->
                    val filtered = input.filter { it.isDigit() }.take(8)
                    loginViewModel.onDOBChanged(filtered)
                },
                "YYYY / MM / DD",
                keyboardType = KeyboardType.Number,
                visualTransformation = DateOfBirthVisualTransformation()
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "성별",
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17
            )

            GenderToggleButton(loginViewModel.isMale) { loginViewModel.onGenderChanged(it) }
        }
        Spacer(Modifier.height(30.dp))
        CTAButton(
            if (loginViewModel.name.isNotEmpty()
                && loginViewModel.dateOfBirth.length == 8
                && loginViewModel.isMale != null
            ) CTAButtonType.GREEN
            else
                CTAButtonType.DISABLED,
            "다음",
            {
                showBottomSheet = true
            })


        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MediCareCallTheme.colors.bg,
                dragHandle = null,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                // Sheet content

                // 개별 약관 아이템 및 상태
                val itemList = listOf(
                    "서비스 이용약관" to Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    "개인정보 수집 및 이용 동의" to Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                var checkedStates by remember { mutableStateOf(List(itemList.size) { false }) }
                val isCheckedAll = checkedStates.all { it }

                Column {
                    Text(
                        "회원가입을 위해\n약관 동의가 필요합니다",
                        color = MediCareCallTheme.colors.black,
                        style = MediCareCallTheme.typography.B_20,
                        modifier = modifier.padding(horizontal = 20.dp, vertical = 30.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var allAgreeCheckState by remember { mutableStateOf(false) }

                        Icon(
                            painterResource(R.drawable.ic_check_box),
                            contentDescription = "체크박스",
                            tint = if (allAgreeCheckState) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = {
                                allAgreeCheckState = !allAgreeCheckState
                                checkedStates = checkedStates.map {
                                    allAgreeCheckState
                                }
                            }
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "전체 동의하기",
                            color = MediCareCallTheme.colors.black,
                            style = MediCareCallTheme.typography.SB_16
                        )

                    }
                }
                HorizontalDivider(thickness = 1.4.dp, color = MediCareCallTheme.colors.gray2)
                Spacer(Modifier.height(12.dp))

                itemList.forEachIndexed { index, (title, modifier) ->
                    AgreementItem(
                        title,
                        isChecked = checkedStates[index],
                        onCheckedChange = {
                            checkedStates = checkedStates.toMutableList().also {
                                it[index] = !it[index]
                            }
                        },
                        modifier = modifier
                    )
                }
                // 모달 내부 CTA 버튼
                CTAButton(
                    if(isCheckedAll) CTAButtonType.GREEN else CTAButtonType.DISABLED,
                    "다음",
                    {},
                    Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 30.dp, top = 20.dp)
                )
            }

        }
    }

}
