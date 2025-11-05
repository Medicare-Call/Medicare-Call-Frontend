package com.konkuk.medicarecall.ui.feature.login.senior.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun ElderChip(
    name: String,
    selected: Boolean,
    onRemove: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = if (selected) MediCareCallTheme.colors.g50 else MediCareCallTheme.colors.bg,
        modifier = modifier
            .border(
                (1.2).dp,
                if (selected) MediCareCallTheme.colors.main
                else MediCareCallTheme.colors.gray3,
                shape = RoundedCornerShape(100.dp),
            )
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick,
            ),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                name,
                color = if (selected) MediCareCallTheme.colors.main
                else MediCareCallTheme.colors.gray3,
                style = MediCareCallTheme.typography.R_14,
                modifier = Modifier
                    .defaultMinSize(minWidth = 38.dp)
                    .padding(start = 4.dp),
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = { onRemove() },
                    ),
                tint = if (selected) {
                    MediCareCallTheme.colors.main
                } else {
                    MediCareCallTheme.colors.gray3
                },
            )
        }
    }
}

@Preview
@Composable
fun ElderChipPreview() {
    MediCareCallTheme {
        ElderChip(name = "", selected = true, onClick = {}, onRemove = {})
    }
}
