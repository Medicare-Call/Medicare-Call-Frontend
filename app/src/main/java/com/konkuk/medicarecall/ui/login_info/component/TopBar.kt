package com.konkuk.medicarecall.ui.login_info.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun TopBar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier.clickable(
                onClick = onClick
            ),
            tint = MediCareCallTheme.colors.black
        )
    }
}