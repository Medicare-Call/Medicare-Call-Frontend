package com.konkuk.medicarecall.ui.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun SettingTextField(category : String,value : String,hint: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        var text by remember { mutableStateOf(value) }
        Text(text = category, color = MediCareCallTheme.colors.gray7, style = MediCareCallTheme.typography.M_17)
        Spacer(modifier = modifier.height(10.dp))
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = value,
            shape = RoundedCornerShape(14.dp),
            onValueChange = {text = it},
            placeholder = {Text(text = hint, color = MediCareCallTheme.colors.gray3, style = MediCareCallTheme.typography.M_16)},
            textStyle = MediCareCallTheme.typography.M_16.copy(
                color = MediCareCallTheme.colors.black
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MediCareCallTheme.colors.white,
                unfocusedContainerColor = MediCareCallTheme.colors.white,
                focusedIndicatorColor = MediCareCallTheme.colors.gray2,
                unfocusedIndicatorColor = MediCareCallTheme.colors.gray2,

            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingTextFieldPreview() {
    SettingTextField("이름","홍길동","이름")
}