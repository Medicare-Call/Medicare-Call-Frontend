package com.konkuk.medicarecall.ui.common.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import android.graphics.Color as AndroidColor

@Composable
fun NameDropdown(
    items: List<String>,
    selectedName: String,
    onDismiss: () -> Unit,
    onItemSelected: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.apply {
                setDimAmount(0f)
                setBackgroundDrawable(AndroidColor.TRANSPARENT.toDrawable())
            }
        }

        BackHandler(enabled = true, onBack = onDismiss)

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onDismiss() },
            )

            // 너비를 가장 긴 아이템에 맞춤
            SubcomposeLayout(
                modifier = Modifier.padding(start = 10.dp, top = 72.dp),
            ) { constraints ->

                val itemPlaceables = subcompose("items") {
                    items.forEach { item ->
                        DropdownItem(
                            name = item,
                            selected = item == selectedName,
                            modifier = Modifier,
                        )
                    }
                }.map { it.measure(Constraints()) }

                val maxWidth = itemPlaceables.maxOfOrNull { it.width } ?: 0
//                val totalHeight = itemPlaceables.sumOf { it.height }

                val finalContent = subcompose("finalContent") {
                    Column(
                        modifier = Modifier
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White),
                    ) {
                        items.forEachIndexed { _, item ->
                            DropdownItem(
                                name = item,
                                selected = item == selectedName,
//                                index = index,
                                modifier = Modifier.width(maxWidth.toDp()),
                                onClick = {
                                    onItemSelected(item)
                                    onDismiss()
                                },
                            )
                        }
                    }
                }[0].measure(constraints)

                layout(finalContent.width, finalContent.height) {
                    finalContent.place(0, 0)
                }
            }
        }
    }
}

@Composable
private fun DropdownItem(
    name: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
//    index: Int = 0,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MediCareCallTheme.typography.SB_18.copy(
                lineHeight = 24.sp,
            ),
            color = if (selected) MediCareCallTheme.colors.black else MediCareCallTheme.colors.gray4,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNameDropdown() {
    MediCareCallTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
        ) {
            NameDropdown(
                items = listOf("김옥자", "박막례", "김헬레나부시크"),
                selectedName = "김옥자",
                onDismiss = {},
                onItemSelected = {},
            )
        }
    }
}
