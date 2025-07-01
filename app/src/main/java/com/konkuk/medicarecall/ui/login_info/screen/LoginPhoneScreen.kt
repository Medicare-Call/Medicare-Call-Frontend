package com.konkuk.medicarecall.ui.login_info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.konkuk.medicarecall.ui.login_info.component.CTAButton
import com.konkuk.medicarecall.ui.login_info.component.CTAButtonType
import com.konkuk.medicarecall.ui.login_info.component.DefaultTextField
import com.konkuk.medicarecall.ui.login_info.component.TopBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun LoginPhoneScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        var text by remember { mutableStateOf("Hello") }

        TopBar({ navController.popBackStack() })
        Spacer(Modifier.height(20.dp))
        Text("휴대폰 번호를\n입력해주세요", style = MediCareCallTheme.typography.B_26)
        Spacer(Modifier.height(40.dp))
        DefaultTextField()
        Spacer(Modifier.height(30.dp))
        CTAButton(color = CTAButtonType.GREEN, "인증번호 받기", {
            // 다음 화면으로 넘어가기, 서버에 인증번호 요청하기
        })

    }
}