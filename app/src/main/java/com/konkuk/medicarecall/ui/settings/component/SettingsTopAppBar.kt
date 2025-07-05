package com.konkuk.medicarecall.ui.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun SettingsTopAppBar(modifier: Modifier = Modifier,
                      title: String,
                      leftIcon: @Composable () -> Unit = {},
                      leftIconClick : () -> Unit = {},
                      rightIcon: @Composable () -> Unit = {},
                      rightIconClick: () -> Unit = {}) {

    Column(modifier = Modifier.fillMaxWidth()) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
        ,
        color = Color.White,
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 12.dp)
        ) {
            IconButton(
                onClick = leftIconClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
            ) {
                leftIcon()
            }
            Text(
                text = title,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center),
                style = MediCareCallTheme.typography.SB_20
            )
            IconButton(
                onClick = rightIconClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            ) {
                rightIcon()
            }
        }
    }
        Box(modifier = Modifier.fillMaxWidth().background(MediCareCallTheme.colors.gray2).height(1.dp))
    }
}

@Preview
@Composable
private fun SettingTopAppBarPreview(
) {
    Box(
        modifier = Modifier
    ) {
        SettingsTopAppBar(
            title = "설정",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            leftIconClick = { /* Handle left icon click */ },
            rightIcon = { /* Add your right icon here */ },
            rightIconClick = { /* Handle right icon click */ }
        )
    }

}