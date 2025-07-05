package com.konkuk.medicarecall.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit,
    category: String? = null,
    placeHolder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    if (category != null) {
        Text(
            category,
            color = MediCareCallTheme.colors.gray7,
            style = MediCareCallTheme.typography.M_17
        )
        Spacer(Modifier.height(10.dp))
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeHolder, style = MediCareCallTheme.typography.M_16) },
        shape = RoundedCornerShape(14.dp),
        visualTransformation = visualTransformation,
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        textStyle = MediCareCallTheme.typography.M_16
    )
}