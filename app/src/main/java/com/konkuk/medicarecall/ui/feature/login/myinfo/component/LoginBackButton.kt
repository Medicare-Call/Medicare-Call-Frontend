package com.konkuk.medicarecall.ui.feature.login.myinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
fun LoginBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .background(MediCareCallTheme.colors.bg),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_settings_back),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = onClick,
                ),
            tint = MediCareCallTheme.colors.black,
        )
    }
}

@Preview
@Composable
private fun Barprev() {
    LoginBackButton({})
}
