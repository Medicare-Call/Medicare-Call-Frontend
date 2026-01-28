package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun CareCallSnackBar(
    modifier: Modifier = Modifier,
    snackBarData: SnackbarData,
) {
    Snackbar(
        modifier = modifier,
        snackbarData = snackBarData,
        containerColor = MediCareCallTheme.colors.black,
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun CareCallSnackBarPreview() {
    MediCareCallTheme {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = MediCareCallTheme.colors.black,
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "케어콜이 완료되었습니다",
                color = Color.White,
            )
        }
    }
}
