package com.konkuk.medicarecall.ui.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun AnnouncementCard(title : String,date : String,modifier: Modifier = Modifier) {
    Column {
        Column(
            modifier = modifier.fillMaxWidth().height(89.dp).padding(20.dp)
                .background(color = MediCareCallTheme.colors.bg)
        ) {
            Text(text = title,style = MediCareCallTheme.typography.SB_16, color = MediCareCallTheme.colors.black)
            Text(text = date, style = MediCareCallTheme.typography.R_15, color = MediCareCallTheme.colors.gray4)
        }
        Box(modifier = modifier.fillMaxWidth().height(1.dp).background(MediCareCallTheme.colors.gray2))
    }
}