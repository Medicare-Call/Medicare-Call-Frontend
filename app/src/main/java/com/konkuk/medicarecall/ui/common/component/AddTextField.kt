package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.gray2
import com.konkuk.medicarecall.ui.theme.main

@Composable
fun AddTextField(
    inputText: String,
    placeHolder: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    clickPlus: () -> Unit = {},
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            modifier = modifier
                .weight(1f),
//                .height(55.dp),
            value = inputText,
            shape = RoundedCornerShape(14.dp),
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = placeHolder,
                    color = MediCareCallTheme.colors.gray3,
                    style = MediCareCallTheme.typography.M_17,
                )
            },
            textStyle = MediCareCallTheme.typography.M_16.copy(
                color = MediCareCallTheme.colors.black,
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MediCareCallTheme.colors.white,
                unfocusedContainerColor = MediCareCallTheme.colors.white,
                focusedIndicatorColor = MediCareCallTheme.colors.main,
                unfocusedIndicatorColor = MediCareCallTheme.colors.gray2,
            ),
            singleLine = true,
            trailingIcon = {
                Box(
                    modifier
                        .padding(end = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (inputText.isBlank()) gray2
                            else main,
                        )
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                if (inputText.isNotBlank())
                                    clickPlus()
                            },
                        ),
                ) {
                    Text(
                        text = "등록",
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
                        color = MediCareCallTheme.colors.white,
                        style = MediCareCallTheme.typography.R_14,
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun AddTextFieldPreview() {
    AddTextField(
        inputText = "d",
        placeHolder = "약을 입력하세요",
        onTextChange = {},
        clickPlus = {},
    )
}
