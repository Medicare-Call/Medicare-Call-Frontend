package com.konkuk.medicarecall.ui.common.component

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Text
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

// 중복, Legacy
@Composable
fun IllnessInfoItem(
    diseaseList: MutableList<String>, modifier: Modifier = Modifier,
    onAddDisease: (String) -> Unit = {}, onRemoveDisease: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val inputTextState = remember { TextFieldState("") }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "질환 정보",
            style = MediCareCallTheme.typography.M_17,
            color = MediCareCallTheme.colors.gray7,
        )
        if (diseaseList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            ) {
                diseaseList.forEachIndexed { index, disease ->
                    ChipItem(
                        text = disease,
                        onRemove = {
                            onRemoveDisease(disease)
                        },
                    )
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
        AddTextField(
            textFieldState = inputTextState,
            placeHolder = "질환명",
            clickPlus = {
                val inputText = inputTextState.text.toString()
                if (inputText.trim().isNotBlank()) {
                    if (diseaseList.contains(inputText)) {
                        Toast
                            .makeText(context, "이미 등록된 질환입니다", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        onAddDisease(inputText)
                    }
                    inputTextState.edit { replace(0, length, "") }
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IllnessInfoItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            IllnessInfoItem(
                diseaseList = mutableListOf("고혈압", "당뇨"),
                onAddDisease = {},
                onRemoveDisease = {},
            )
        }
    }
}
