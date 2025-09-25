package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun AddTextField(
    inputText: String,
    placeHolder: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    clickPlus: () -> Unit = {}
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
                    style = MediCareCallTheme.typography.M_17
                )
            },
            textStyle = MediCareCallTheme.typography.M_16.copy(
                color = MediCareCallTheme.colors.black
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MediCareCallTheme.colors.white,
                unfocusedContainerColor = MediCareCallTheme.colors.white,
                focusedIndicatorColor = MediCareCallTheme.colors.main,
                unfocusedIndicatorColor = MediCareCallTheme.colors.gray2,
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = clickPlus) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "추가 아이콘",
                        modifier = modifier.size(20.dp),
                        tint = MediCareCallTheme.colors.black
                    )
                }
            }
        )
    }
}
