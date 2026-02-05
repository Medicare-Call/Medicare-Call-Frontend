package com.konkuk.medicarecall.ui.feature.settings.mydata.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun LogoutConfirmDialog(onDismiss: () -> Unit, onLogout: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(MediCareCallTheme.colors.white)
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "정말 로그아웃할까요?",
                style = MediCareCallTheme.typography.SB_18,
                color = MediCareCallTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediCareCallTheme.colors.gray1,
                        contentColor = MediCareCallTheme.colors.gray6,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                ) {
                    Text("취소", style = MediCareCallTheme.typography.B_17)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = onLogout,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediCareCallTheme.colors.negative,
                        contentColor = MediCareCallTheme.colors.white,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                ) {
                    Text("로그아웃", style = MediCareCallTheme.typography.B_17)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogoutDialogPreview() {
    LogoutConfirmDialog(
        onDismiss = {},
        onLogout = {},
    )
}
