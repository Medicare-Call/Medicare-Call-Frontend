package com.konkuk.medicarecall.ui.common.component

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Text
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun DiseaseNamesItem(
    textState: TextFieldState,
    diseaseList: List<String>,
    onAddDisease: (String) -> Unit,
    onRemoveChip: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                diseaseList.forEach { disease ->
                    ChipItem(
                        text = disease,
                        onRemove = {
                            onRemoveChip(disease)
                        },
                    )
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
        AddTextField(
            textFieldState = textState,
            placeHolder = "질환명",
            clickPlus = {
                val input = textState.text.toString()
                if (input.trim().isNotBlank()) {
                    if (!diseaseList.contains(input)) {
                        onAddDisease(input)
                    }
                    textState.edit { replace(0, length, "") }
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiseaseNamesItemPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            DiseaseNamesItem(
                textState = TextFieldState(""),
                diseaseList = listOf("고혈압", "당뇨"),
                onAddDisease = {},
                onRemoveChip = {},
            )
        }
    }
}
