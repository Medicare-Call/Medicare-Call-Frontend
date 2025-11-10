package com.konkuk.medicarecall.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun HomeMealContainer(
    modifier: Modifier = Modifier,
    breakfastEaten: Boolean?,
    lunchEaten: Boolean?,
    dinnerEaten: Boolean?,
    onClick: () -> Unit,
) {
    fun getRiceBowlIcon(isEaten: Boolean?): Int {
        return when (isEaten) {
            true -> R.drawable.ic_ricebowl_eat // 밥 먹음
            false -> R.drawable.ic_ricebowl_skip // 밥 안먹음
            null -> R.drawable.ic_ricebowl_uncheck // 미기록
        }
    }

    Card(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .figmaShadow(
                group = LocalMediCareCallShadowProvider.current.shadow01,
                cornerRadius = 14.dp,
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            // 1) Title: 식사
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.ic_ricebowl),
                    contentDescription = "ricebowl icon",
                )
                Spacer(Modifier.width(8.dp))

                Text(
                    "식사",
                    style = MediCareCallTheme.typography.R_16,
                    color = MediCareCallTheme.colors.gray8,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2) 아침 점심 저녁
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 아침
                Column(
                    modifier = Modifier
                        .padding(horizontal = 7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(id = getRiceBowlIcon(breakfastEaten)),
                        contentDescription = "아침 식사 기록",
                    )

                    Text(
                        "아침",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray6,
                    )
                }

                // 점심
                Column(
                    modifier = Modifier.padding(horizontal = 7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(id = getRiceBowlIcon(lunchEaten)),
                        contentDescription = "점심 식사 기록",
                    )

                    Text(
                        "점심",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray6,
                    )
                }
                // 저녁
                Column(
                    modifier = Modifier.padding(horizontal = 7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(id = getRiceBowlIcon(dinnerEaten)),
                        contentDescription = "저녁 식사 기록",
                    )

                    Text(
                        "저녁",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray6,
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun PreviewHomeMealContainer() {
    HomeMealContainer(
        breakfastEaten = true,
        lunchEaten = false,
        dinnerEaten = null,
        onClick = {},
    )
}
