package com.konkuk.medicarecall.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.login_info.component.GenderToggleButton
import com.konkuk.medicarecall.ui.settings.component.SettingTextField
import com.konkuk.medicarecall.ui.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.nio.file.Files.size

@Composable
fun PersonalDetailScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MediCareCallTheme.colors.bg)) {
        SettingsTopAppBar(
            title = "어르신 건강정보 설정",
            leftIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "setting back"
                )
            },
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "삭제", color = MediCareCallTheme.colors.negative,  style = MediCareCallTheme.typography.SB_16)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SettingTextField("성함", "김옥자", "성함")
                SettingTextField("생년월일","1939 / 09 / 18", "YYYY / MM / DD")
                Column() {
                    Text("성별", style = MediCareCallTheme.typography.M_17, color = MediCareCallTheme.colors.gray7)
                    Spacer(modifier = modifier.height(10.dp))
                    GenderToggleButton()
                }
                SettingTextField("휴대폰 번호","010-1111-1111","010-1234-5678")
                Column() {
                    Text("어르신과의 관계",)
                    Spacer(modifier = modifier.height(10.dp))
                    // 드롭다운
                }
                Column() {
                    Text("어르신 거주방식", )
                    Spacer(modifier = modifier.height(10.dp))
                    // 드롭다운
                }

                Button(
                    modifier = modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MediCareCallTheme.colors.white,
                        containerColor = MediCareCallTheme.colors.main
                    )

                ) {
                    Text("확인")
                }

            }


        }
    }
}

@Preview
@Composable
private fun PersonalDetailPreview() {
    PersonalDetailScreen()

}