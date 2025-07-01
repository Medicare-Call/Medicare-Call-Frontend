package com.konkuk.medicarecall.ui.login_info.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.util.PhoneNumberVisualTransformation


@Composable
fun PhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() }.take(11)
            onValueChange(filtered)
        },
        visualTransformation =
            PhoneNumberVisualTransformation(),
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
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true
        )
}