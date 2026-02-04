package com.konkuk.medicarecall.ui.feature.homedetail.meal.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.LocalMediCareCallShadowProvider
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow

@Composable
fun MealDetailCard(
    mealTime: String,
    description: String,
    isRecorded: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
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
            // 1) 아침 점심 저녁
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    mealTime,
                    style = MediCareCallTheme.typography.SB_18,
                    color = MediCareCallTheme.colors.gray8,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 2) 식사 내용 및 여부
                if (isRecorded) {
                    Text(
                        text = description,
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                } else {
                    // 기록 전
                    Text(
                        text = "식사 기록 전이에요.",
                        style = MediCareCallTheme.typography.R_16,
                        color = MediCareCallTheme.colors.gray8,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMealDetailCard() {
    MealDetailCard(
        mealTime = "아침",
        description = "간단히 밥과 반찬을 드셨어요.",
        isRecorded = true,
    )
}
