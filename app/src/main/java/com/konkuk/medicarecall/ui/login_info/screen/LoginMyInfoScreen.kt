package com.konkuk.medicarecall.ui.login_info.screen

import android.R.attr.checked
import android.R.id.input
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.model.LoginUiState
import com.konkuk.medicarecall.ui.login_info.component.AgreementItem
import com.konkuk.medicarecall.ui.login_info.component.CTAButton
import com.konkuk.medicarecall.ui.login_info.component.CTAButtonType
import com.konkuk.medicarecall.ui.login_info.component.DefaultTextField
import com.konkuk.medicarecall.ui.login_info.component.GenderToggleButton
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.login_info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.util.DateOfBirthVisualTransformation
import kotlin.text.isDigit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginMyInfoScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {

    val loginUiState = loginViewModel.loginUiState.collectAsState()
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
            GenderToggleButton()
        }
        Spacer(Modifier.height(30.dp))
        CTAButton(CTAButtonType.GREEN, "다음", {
            showBottomSheet = true
        })


        val sheetState = rememberModalBottomSheetState()


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MediCareCallTheme.colors.bg,
                dragHandle = null
            ) {
                // Sheet content

                Column {
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
                            Icon(
                                painterResource(R.drawable.ic_check_box),
                                contentDescription = "체크박스"
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
                    AgreementItem("서비스 이용약관", Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                    AgreementItem(
                        "개인정보 수집 및 이용동의",
                        Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                // CTAButton(CTAButtonType.GREEN, "다음", {}, Modifier.padding(bottom = 30.dp))
            }

        }
    }

}
