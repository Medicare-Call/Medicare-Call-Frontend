package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    textFieldState: TextFieldState,
    placeHolder: String,
    modifier: Modifier = Modifier,
    clickPlus: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            state = textFieldState,
            modifier = Modifier.weight(1f),
            textStyle = MediCareCallTheme.typography.M_16.copy(
                color = MediCareCallTheme.colors.black,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            interactionSource = interactionSource,
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(MediCareCallTheme.colors.white)
                        .border(
                            width = 1.dp,
                            color = if (isFocused) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                            shape = shape,
                        )
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (textFieldState.text.isEmpty()) {
                                Text(
                                    text = placeHolder,
                                    color = MediCareCallTheme.colors.gray3,
                                    style = MediCareCallTheme.typography.M_17,
                                )
                            }
                            innerTextField()
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (textFieldState.text.isBlank()) gray2 else main,
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = null,
                                    onClick = {
                                        if (textFieldState.text.isNotBlank()) {
                                            clickPlus()
                                        }
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
                    }
                }
            },
        )
    }
}

@Preview
@Composable
fun AddTextFieldPreview() {
    MediCareCallTheme {
        AddTextField(
            textFieldState = TextFieldState("d"),
            placeHolder = "약을 입력하세요",
            clickPlus = {},
        )
    }
}
