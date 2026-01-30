package com.konkuk.medicarecall.ui.feature.login.calltime.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun CallTimeBenefit(content: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_present),
            contentDescription = "benefit_icon",
            modifier = modifier.size(14.dp),
            tint = Color.Unspecified, // Use default tint
        )
        Spacer(modifier = modifier.width(6.dp))
        Text(
            text = content,
            style = MediCareCallTheme.typography.R_14,
            color = MediCareCallTheme.colors.gray5,
            modifier = modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallTimeBenefitPreview() {
    MediCareCallTheme {
        Column(Modifier.padding(16.dp)) {
            CallTimeBenefit(content = "어르신 맞춤형 AI 건강관리")
            Spacer(Modifier.height(8.dp))
            CallTimeBenefit(content = "1:1 전담 케어매니저 배정")
        }
    }
}
