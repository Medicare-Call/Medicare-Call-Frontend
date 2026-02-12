package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.domain.model.type.GenderType

@Composable
fun GenderToggleButton(
    modifier: Modifier = Modifier,
    selectedGenderType: GenderType? = null,
    onGenderChange: (GenderType) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(RoundedCornerShape(14.dp))
            .background(
                MediCareCallTheme.colors.white,
            ),
    ) {
        GenderType.entries.forEach { gender ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (gender == selectedGenderType) MediCareCallTheme.colors.g50
                        else MediCareCallTheme.colors.white,
                    )
                    .border(
                        width = 1.2.dp,
                        color = if (gender == selectedGenderType) MediCareCallTheme.colors.main
                        else MediCareCallTheme.colors.gray2,
                        shape = if (gender == GenderType.MALE) RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                        else RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp),
                    )
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = { onGenderChange(gender) },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = if (gender == selectedGenderType) 15.5.dp
                            else 16.dp,
                        ),
                    text = gender.displayName,
                    color = if (gender == selectedGenderType) MediCareCallTheme.colors.main
                    else MediCareCallTheme.colors.black,
                    style = if (gender == selectedGenderType) MediCareCallTheme.typography.B_17
                    else MediCareCallTheme.typography.M_16,
                )
            }
        }
    }
}

@Composable
fun GenderToggleButton(
    isMale: Boolean = true,
    onGenderChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(RoundedCornerShape(14.dp))
            .background(
                MediCareCallTheme.colors.white,
            ),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = if (isMale) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.white)
                .border(
                    width = 1.2.dp,
                    color = if (isMale) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                    shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp),
                )
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { onGenderChange(true) },
                ),

            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "남성",
                color = if (isMale) MediCareCallTheme.colors.main else MediCareCallTheme.colors.black,
                style = if (isMale) MediCareCallTheme.typography.B_17 else MediCareCallTheme.typography.M_16,
                modifier = Modifier
                    .padding(vertical = if (isMale) 15.5.dp else 16.dp),
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = if (!isMale) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.white)
                .border(
                    width = 1.2.dp,
                    color = if (!isMale) MediCareCallTheme.colors.main else MediCareCallTheme.colors.gray2,
                    shape = RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp),
                )
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { onGenderChange(false) },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "여성",
                color = if (!isMale) MediCareCallTheme.colors.main else MediCareCallTheme.colors.black,
                style = if (!isMale) MediCareCallTheme.typography.B_17 else MediCareCallTheme.typography.M_16,
                modifier = Modifier
                    .padding(vertical = if (!isMale) 15.5.dp else 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenderToggleButtonPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            GenderToggleButton(
                isMale = true,
                onGenderChange = {},
            )
        }
    }
}
