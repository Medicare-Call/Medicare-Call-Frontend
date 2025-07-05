package com.konkuk.medicarecall.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.model.CTAButtonType
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme

@Composable
// CTAButtonType을 이용해 흰 버튼인지 초록 버튼인지 정해지고,
// 클릭 시에 버튼 색상이 바뀜.
fun CTAButton(
    type: CTAButtonType,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val backgroundColor = when (type) {
        CTAButtonType.WHITE -> MediCareCallTheme.colors.white
        CTAButtonType.GREEN -> MediCareCallTheme.colors.main
        CTAButtonType.DISABLED -> MediCareCallTheme.colors.g200
    }

    val textColor = when (type) {
        CTAButtonType.WHITE -> MediCareCallTheme.colors.main
        CTAButtonType.GREEN -> MediCareCallTheme.colors.white
        CTAButtonType.DISABLED -> MediCareCallTheme.colors.white
    }

    val clickedColor = when (type) {
        CTAButtonType.WHITE -> MediCareCallTheme.colors.gray1
        CTAButtonType.GREEN -> MediCareCallTheme.colors.g500
        CTAButtonType.DISABLED -> MediCareCallTheme.colors.g200
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // isPressed 상태에 따라 버튼 색상 결정
    val buttonColor = if (isPressed) clickedColor else backgroundColor


    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = buttonColor, shape = RoundedCornerShape(14.dp))
            .clickable(

                interactionSource,
                onClick = onClick,
                indication = null,
                enabled = type != CTAButtonType.DISABLED
            )
    ) {
        Text(
            text = text,
            color = textColor,
            style = MediCareCallTheme.typography.B_17,
            modifier = Modifier.padding(vertical = 16.dp).align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun CTAButtonPreview() {
    CTAButton(CTAButtonType.GREEN, "시작하기", {})
}