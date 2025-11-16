package com.konkuk.medicarecall.ui.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun PersonalInfoCard(name: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .figmaShadow(
                group = MediCareCallTheme.shadow.shadow01,
                cornerRadius = 14.dp,
            )
            .clip(RoundedCornerShape(14.dp))
            .background(MediCareCallTheme.colors.white)
            .clickable { onClick() },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Text(
                    text = name,
                    style = MediCareCallTheme.typography.SB_16,
                    color = MediCareCallTheme.colors.gray8,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "어르신",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "화살표 아이콘",
                modifier = Modifier.size(28.dp),
                tint = MediCareCallTheme.colors.gray2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalInfoCardPrev() {
    Column(Modifier.fillMaxSize()) {
//        PersonalInfoCard("김옥자")
    }
}
