package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun CareCallFloatingButton(
    modifier: Modifier = Modifier,
    careCallOption: String,
    text: String,
    onClick: () -> Unit = {},
) {
    careCallOption // TODO: 현재 사용되지 않는 변수입니다. 추후 기능 추가 시 활용할 수 있습니다.

    Button(
        modifier = modifier,
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MediCareCallTheme.colors.main,
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_carecall),
                contentDescription = "Care Call",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MediCareCallTheme.typography.SB_16,
                color = Color.White,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CareCallFloatingButtonPreview() {
    MediCareCallTheme {
        CareCallFloatingButton(
            careCallOption = "immediate",
            text = "즉시 케어콜",
        )
    }
}
