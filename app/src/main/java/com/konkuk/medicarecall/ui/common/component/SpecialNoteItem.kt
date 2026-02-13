package com.konkuk.medicarecall.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.theme.figmaShadow
import kotlinx.coroutines.delay

// 미사용, Legacy
@Composable
fun <T> SpecialNoteItem(
//    modifier: Modifier = Modifier,
    enumList: List<T>, // Enum.entries.map { it. displayName }.toList() 전달
    noteList: List<String>,
    onAddNote: (String) -> Unit,
    onRemoveNote: (String) -> Unit,
    placeHolder: String,
    category: String? = null,
    scrollState: ScrollState,
) {
    var showDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(showDropdown) {
        if (showDropdown) {
            delay(250L)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    if (category != null) {
        Text(
            category,
            color = MediCareCallTheme.colors.gray7,
            style = MediCareCallTheme.typography.M_17,
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (noteList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        ) {
            noteList.forEach { note ->
                ChipItem(
                    text = note,
                    onRemove = {
                        onRemoveNote(note)
                    },
                )
                Spacer(Modifier.width(10.dp))
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Column {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        showDropdown = !showDropdown
                    },
                ),
            placeholder = {
                Text(placeHolder, style = MediCareCallTheme.typography.M_16)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = MediCareCallTheme.colors.white,
                disabledPlaceholderColor = MediCareCallTheme.colors.gray3,
                disabledBorderColor = MediCareCallTheme.colors.gray2,
                disabledTextColor = MediCareCallTheme.colors.black,
            ),
            readOnly = true,
            trailingIcon = {
                Icon(
                    painterResource(if (showDropdown) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down_small),
                    contentDescription = "드롭다운 화살표",
                    tint = MediCareCallTheme.colors.black,
                )
            },
            singleLine = true,
            textStyle = MediCareCallTheme.typography.M_17,
        )

        AnimatedVisibility(visible = showDropdown) {
            val dropdownScrollState = rememberScrollState()

            Box(
                Modifier
                    .fillMaxHeight()
                    .padding(top = 8.dp)
                    .figmaShadow(
                        group = MediCareCallTheme.shadow.shadow01,
                        cornerRadius = 14.dp,
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(MediCareCallTheme.colors.white)
                    .border(
                        1.2.dp,
                        shape = RoundedCornerShape(14.dp),
                        color = MediCareCallTheme.colors.gray1,
                    )
                    .heightIn(max = 215.dp)
                    .verticalScroll(dropdownScrollState),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MediCareCallTheme.colors.white),
                ) {
                    enumList.forEach { item ->
                        val itemStr = item.toString()
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 1.2.dp.toPx()
                                    val y = size.height - strokeWidth / 2f

                                    drawLine(
                                        color = Color(0xFFECECEC), // NavigationBar의 상단 테두리
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth,
                                    )
                                }
                                .clickable(
                                    onClick = {
                                        showDropdown = false
                                        if (!noteList.contains(itemStr)) {
                                            onAddNote(itemStr)
                                        }
                                    },
                                ),
                        ) {
                            Text(
                                item.toString(),
                                color = MediCareCallTheme.colors.gray8,
                                style = MediCareCallTheme.typography.M_16,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

// @Preview(showBackground = true)
// @Composable
// private fun SpecialNotePreview() {
//     val scrollState = rememberScrollState()
//     var noteList by remember { mutableStateOf(listOf<String>()) }
//
//     Box(
//         modifier = Modifier
//             .fillMaxSize()
//             .verticalScroll(scrollState)
//             .padding(16.dp)
//     ) {
//         SpecialNoteItem(
//             enumList = SpecialNoteType.entries.map { it.displayName },
//             noteList = noteList,
//             onAddNote = { noteList = noteList + it },
//             onRemoveNote = { noteList = noteList - it },
//             placeHolder = "특이사항 선택하기",
//             category = "특이사항",
//             scrollState = scrollState
//         )
//     }
// }
