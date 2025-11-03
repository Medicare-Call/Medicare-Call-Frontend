package com.konkuk.medicarecall.ui.common.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Text
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

// 중복, Legacy
@Composable
fun IllnessInfoItem(
    diseaseList: MutableList<String>, modifier: Modifier = Modifier,
    onAddDisease: (String) -> Unit = {}, onRemoveDisease: (String) -> Unit = {},
) {
    Log.d("IllnessInfoItem", "diseaseList: $diseaseList")
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
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
            inputText = inputText,
            placeHolder = "질환명",
            onTextChange = { inputText = it },
            clickPlus = {
                if (inputText.trim().isNotBlank()) {
                    if (diseaseList.contains(inputText)) {
                        Toast
                            .makeText(context, "이미 등록된 질환입니다", Toast.LENGTH_SHORT)
                            .show()
                        inputText = ""
                    } else {
                        onAddDisease(inputText)
                        inputText = ""
                    }
                }
            },
        )
    }
}
