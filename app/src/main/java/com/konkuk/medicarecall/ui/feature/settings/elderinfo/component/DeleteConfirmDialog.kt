package com.konkuk.medicarecall.ui.feature.settings.elderinfo.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DeleteConfirmDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(MediCareCallTheme.colors.white)
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "정말 삭제할까요?",
                style = MediCareCallTheme.typography.SB_18,
                color = MediCareCallTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "어르신 개인정보와 건강정보를 삭제하면 되돌릴 수 없어요.",
                style = MediCareCallTheme.typography.R_16,
                color = MediCareCallTheme.colors.gray7,
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
                    onClick = onDelete,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediCareCallTheme.colors.negative,
                        contentColor = MediCareCallTheme.colors.white,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                ) {
                    Text("삭제", style = MediCareCallTheme.typography.B_17)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteDialogPreview() {
    var showDeleteDialog by remember { mutableStateOf(true) }
    // ✅ 다이얼로그 표시
    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                showDeleteDialog = false
                // TODO: 삭제 동작 추가
            },
        )
    }
}
