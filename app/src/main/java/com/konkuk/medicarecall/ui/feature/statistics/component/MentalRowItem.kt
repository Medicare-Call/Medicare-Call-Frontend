package com.konkuk.medicarecall.ui.feature.statistics.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun MentalRowItem(
    label: String,
    count: Int,
    iconRes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier,
    ) {
        Text(
            text = label,
            style = MediCareCallTheme.typography.R_14,
            color = MediCareCallTheme.colors.gray6,
        )
        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${count}번",
            style = MediCareCallTheme.typography.R_14,
            color = MediCareCallTheme.colors.gray6,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMentalRowItem() {
    MentalRowItem(
        label = "좋음",
        count = 4,
        iconRes = R.drawable.ic_emoji_good,
    )
}
