package com.konkuk.medicarecall.ui.login_info.component

import android.R.attr.value
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DefaultTextField(modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("휴대폰 번호", style = MediCareCallTheme.typography.M_16) },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MediCareCallTheme.colors.white,
            unfocusedPlaceholderColor = MediCareCallTheme.colors.gray3,
            unfocusedBorderColor = MediCareCallTheme.colors.gray2,
            unfocusedTextColor = MediCareCallTheme.colors.black,
            focusedContainerColor = MediCareCallTheme.colors.white,
            focusedTextColor = MediCareCallTheme.colors.black,
            focusedBorderColor = MediCareCallTheme.colors.main,
            focusedPlaceholderColor = MediCareCallTheme.colors.gray3,



        )
    )
}

@Preview
@Composable
private fun DefaultTextFieldPreview() {
    DefaultTextField()
}