package com.konkuk.medicarecall.ui.login_info.component

import android.R.attr.thickness
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun GenderToggleButton() {

    var isMale by remember { mutableStateOf<Boolean?>(null) }



    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
//            .border(
//                BorderStroke(1.2.dp, MediCareCallTheme.colors.gray2), // 전체 테두리
//                RoundedCornerShape(14.dp)
//            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                MediCareCallTheme.colors.white
            )
    ) {
        Box(
            Modifier
                .weight(1f)
                .background(color = if (isMale == true) MediCareCallTheme.colors.g100 else MediCareCallTheme.colors.white)
                .border(
                    width = 1.2.dp,
                    color = if (isMale == true) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                    shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                )
                .clickable(
                    onClick = { isMale = true },

                ),

            contentAlignment = Alignment.Center
        ) {
            Text(
                "남성",
                color = if (isMale == true) MediCareCallTheme.colors.main else MediCareCallTheme.colors.black,
                style = if (isMale == true) MediCareCallTheme.typography.B_17 else MediCareCallTheme.typography.M_16,
                modifier = Modifier
                    .padding(vertical = if (isMale == true) 15.5.dp else 16.dp)
            )
        }

        VerticalDivider(thickness = 1.2.dp, color = MediCareCallTheme.colors.gray2)

        Box(
            Modifier
                .weight(1f)
                .background(color = if (isMale == false) MediCareCallTheme.colors.g100 else MediCareCallTheme.colors.white)
                .border(
                    width = 1.2.dp,
                    color = if (isMale == false) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                    shape = RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp)
                )
                .clickable(
                    onClick = { isMale = false }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "여성",
                color = if (isMale == false) MediCareCallTheme.colors.main else MediCareCallTheme.colors.black,
                style = if (isMale == false) MediCareCallTheme.typography.B_17 else MediCareCallTheme.typography.M_16,
                modifier = Modifier
                    .padding(vertical = if (isMale == false) 15.5.dp else 16.dp)

            )
        }
    }
}

@Preview
@Composable
private fun GenderToggleButtonPreview() {
    GenderToggleButton()
}