package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit,
    category: String? = null,
    placeHolder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int = Int.MAX_VALUE,
    textFieldModifier: Modifier = Modifier,
) {
    if (category != null) {
        Text(
            category,
            color = MediCareCallTheme.colors.gray7,
            style = MediCareCallTheme.typography.M_17,
        )
        Spacer(Modifier.height(10.dp))
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = textFieldModifier.fillMaxWidth(),
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
        textStyle = MediCareCallTheme.typography.M_16,
    )
}

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
    title: String? = null,
    placeHolder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    lineLimits: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isError -> MediCareCallTheme.colors.negative
        isFocused -> MediCareCallTheme.colors.main
        else -> MediCareCallTheme.colors.gray2
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (title != null) {
            Text(
                text = title,
                color = MediCareCallTheme.colors.gray7,
                style = MediCareCallTheme.typography.M_17,
            )
        }

        BasicTextField(
            state = textFieldState,
            enabled = enabled,
            inputTransformation = inputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = if (lineLimits == 1) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(lineLimits),
            interactionSource = interactionSource,
            textStyle = MediCareCallTheme.typography.M_16.copy(
                color = MediCareCallTheme.colors.black,
            ),
            outputTransformation = outputTransformation,
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MediCareCallTheme.colors.white,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .border(
                            width = 1.2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = MediCareCallTheme.typography.M_16,
                            color = MediCareCallTheme.colors.gray3,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultTextFieldPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            DefaultTextField(
                value = "",
                onValueChange = {},
                category = "이름",
                placeHolder = "이름을 입력하세요",
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
private fun DefaultTextFieldStatePreview() {
    MediCareCallTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DefaultTextField(
                title = "기본 상태",
                placeHolder = "입력하세요",
            )
            DefaultTextField(
                textFieldState = TextFieldState("입력된 텍스트"),
                title = "입력된 상태",
                placeHolder = "입력하세요",
            )
            DefaultTextField(
                title = "에러 상태",
                placeHolder = "입력하세요",
                isError = true,
            )
            // 포커스 상태 시뮬레이션
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "포커스 상태",
                    color = MediCareCallTheme.colors.gray7,
                    style = MediCareCallTheme.typography.M_17,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MediCareCallTheme.colors.white,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .border(
                            width = 1.2.dp,
                            color = MediCareCallTheme.colors.main,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "입력 중...",
                        style = MediCareCallTheme.typography.M_16,
                        color = MediCareCallTheme.colors.black,
                    )
                }
            }
        }
    }
}
