package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Text
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun GlucoseStatusItem(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(8.dp),
                painter = painterResource(id = R.drawable.ic_glucose_low),
                contentDescription = "low level",
                tint = MediCareCallTheme.colors.active,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "낮음",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray5,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(8.dp),
                painter = painterResource(id = R.drawable.ic_glucose_normal),
                contentDescription = "normal level",
                tint = MediCareCallTheme.colors.positive,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "정상",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray5,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(8.dp),
                painter = painterResource(id = R.drawable.ic_glucose_high),
                contentDescription = "high level",
                tint = MediCareCallTheme.colors.negative,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "높음",
                style = MediCareCallTheme.typography.R_14,
                color = MediCareCallTheme.colors.gray5,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGlucoseStatusItem() {
    GlucoseStatusItem()
}
