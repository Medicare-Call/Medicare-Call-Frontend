package com.konkuk.medicarecall.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ShadowLayer(
    val offsetX: Dp,
    val offsetY: Dp,
    val blurRadius: Dp,
    val color: Color
)

@Immutable
data class ShadowGroup(
    val layers: List<ShadowLayer>
)

@Immutable
data class MediCareCallShadows(
    val shadow01: ShadowGroup,
    val shadow02: ShadowGroup,
    val shadow03: ShadowGroup,
)

val defaultMediCareCallShadow = MediCareCallShadows(
    shadow01 = ShadowGroup(
        listOf(
            ShadowLayer(0.dp, 4.dp, 8.dp, Color(0xFF222222).copy(alpha = 0.04f)),
            ShadowLayer(4.dp, 0.dp, 8.dp, Color(0xFF222222).copy(alpha = 0.08f)),
            ShadowLayer(-4.dp, 0.dp, 8.dp, Color(0xFF222222).copy(alpha = 0.08f)),
        )
    ),

    shadow02 = ShadowGroup(
        listOf(
            ShadowLayer(0.dp, 4.dp, 12.dp, Color(0xFF222222).copy(alpha = 0.04f)),
            ShadowLayer(4.dp, 0.dp, 12.dp, Color(0xFF222222).copy(alpha = 0.08f)),
            ShadowLayer(-4.dp, 0.dp, 12.dp, Color(0xFF222222).copy(alpha = 0.08f)),
        )
    ),

    shadow03 = ShadowGroup(
        listOf(
            ShadowLayer(0.dp, 4.dp, 16.dp, Color(0xFF222222).copy(alpha = 0.04f)),
            ShadowLayer(4.dp, 0.dp, 16.dp, Color(0xFF222222).copy(alpha = 0.08f)),
            ShadowLayer(-4.dp, 0.dp, 16.dp, Color(0xFF222222).copy(alpha = 0.08f)),
        )
    )
)

val LocalMediCareCallShadowProvider = staticCompositionLocalOf { defaultMediCareCallShadow }

fun Modifier.figmaShadow(
    group: ShadowGroup,
    cornerRadius: Dp = 14.dp
): Modifier = this.drawWithCache {
    val radiusPx = cornerRadius.toPx()
    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = 0 // 실제 배경색은 drawContent() 이후에 그릴 거라 영 투명
    }

    onDrawWithContent {
        // 1) 레이어별로 그림자 그리기
        group.layers.forEach { layer ->
            paint.setShadowLayer(
                layer.blurRadius.toPx(),
                layer.offsetX.toPx(),
                layer.offsetY.toPx(),
                layer.color.toArgb()
            )
            drawIntoCanvas { canvas ->
                // 안드로이드 네이티브 Canvas
                val nc = canvas.nativeCanvas
                nc.drawRoundRect(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    radiusPx,
                    radiusPx,
                    paint
                )
            }
        }
        // 2) 그림자 레이어 제거
        paint.clearShadowLayer()
        // 3) 실제 배경(흰색 등) 그리기
        drawIntoCanvas { canvas ->
            // 안드로이드 네이티브 Canvas
            val nc = canvas.nativeCanvas
//            paint.color  = Color.White.toArgb() // 배경색 설정(흰색으로 설정)
            nc.drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                radiusPx,
                radiusPx,
                paint
            )
        }
        // 4) 자식 컴포저블(텍스트, 아이콘 등) 그리기
        drawContent()
    }
}



