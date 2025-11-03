package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.konkuk.medicarecall.ui.common.util.GlucoseLevel
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.common.util.classifyGlucose
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun GlucoseStatusChip(
    value: Int,
    timing: GlucoseTiming,
) {
    val level = classifyGlucose(value.toFloat(), timing)
    val (text, color) = when (level) {
        GlucoseLevel.LOW -> "낮음" to MediCareCallTheme.colors.active
        GlucoseLevel.NORMAL -> "정상" to MediCareCallTheme.colors.main
        GlucoseLevel.HIGH -> "높음" to MediCareCallTheme.colors.negative
    }

    Card(
        modifier = Modifier
            .height(30.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MediCareCallTheme.typography.R_16,
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewGlucoseStatusChip_Before() {
    GlucoseStatusChip(value = 89, timing = GlucoseTiming.BEFORE_MEAL)
}

@Preview
@Composable
private fun PreviewGlucoseStatusChip_After() {
    GlucoseStatusChip(value = 150, timing = GlucoseTiming.AFTER_MEAL)
}
