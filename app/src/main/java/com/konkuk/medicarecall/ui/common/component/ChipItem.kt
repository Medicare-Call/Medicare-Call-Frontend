package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun ChipItem(text: String, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = MediCareCallTheme.colors.g50,
        modifier = Modifier
            .border((1.2).dp, MediCareCallTheme.colors.main, shape = RoundedCornerShape(100.dp))
            .padding(),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text,
                color = MediCareCallTheme.colors.main,
                style = MediCareCallTheme.typography.R_14,
                modifier = Modifier.padding(start = 4.dp),
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() },
                tint = MediCareCallTheme.colors.main,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChipItemPreview() {
    MediCareCallTheme {
        Row(Modifier.padding(16.dp)) {
            ChipItem(text = "고혈압", onRemove = {})
            Spacer(Modifier.width(8.dp))
            ChipItem(text = "당뇨", onRemove = {})
        }
    }
}
