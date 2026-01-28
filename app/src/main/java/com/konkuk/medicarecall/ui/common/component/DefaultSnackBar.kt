package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DefaultSnackBar(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(
        hostState,
        modifier = modifier,
        snackbar = {
            Box(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MediCareCallTheme.colors.black)
                    .fillMaxWidth(),
            ) {
                Text(
                    it.visuals.message,
                    modifier = Modifier.padding(14.dp),
                    style = MediCareCallTheme.typography.R_14,
                    color = MediCareCallTheme.colors.white,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultSnackBarPreview() {
    MediCareCallTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MediCareCallTheme.colors.black)
                    .fillMaxWidth(),
            ) {
                Text(
                    "스낵바 메시지 예시",
                    modifier = Modifier.padding(14.dp),
                    style = MediCareCallTheme.typography.R_14,
                    color = MediCareCallTheme.colors.white,
                )
            }
        }
    }
}
