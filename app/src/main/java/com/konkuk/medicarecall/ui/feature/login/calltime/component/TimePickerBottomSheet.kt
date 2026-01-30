package com.konkuk.medicarecall.ui.feature.login.calltime.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.common.component.CTAButton
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import com.konkuk.medicarecall.ui.type.CTAButtonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    visible: Boolean,
    initialTabIndex: Int = 0,
    initialFirstHour: Int = 1,
    initialFirstMinute: Int = 0,
    initialSecondHour: Int = 1,
    initialSecondMinute: Int = 0,
    initialThirdHour: Int = 1,
    initialThirdMinute: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (
        firstHour: Int, firstMinute: Int,
        secondHour: Int, secondMinute: Int,
        thirdHour: Int, thirdMinute: Int,
    ) -> Unit,
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // 1차, 2차 각각의 시간 상태 관리
    var firstHour by remember { mutableIntStateOf(initialFirstHour) }
    var firstMinute by remember { mutableIntStateOf(initialFirstMinute) }

    var secondHour by remember { mutableIntStateOf(initialSecondHour) }
    var secondMinute by remember { mutableIntStateOf(initialSecondMinute) }

    var thirdHour by remember { mutableIntStateOf(initialThirdHour) }
    var thirdMinute by remember { mutableIntStateOf(initialThirdMinute) }

    // 탭 구성
    var tabIndex by remember { mutableIntStateOf(initialTabIndex) }

    val tabs = listOf("1차", "2차", "3차")
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MediCareCallTheme.colors.bg,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.Transparent,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediCareCallTheme.colors.bg)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "시간 설정",
                style = MediCareCallTheme.typography.M_20,
                color = MediCareCallTheme.colors.black,
            )
            Spacer(modifier = Modifier.height(18.dp))

            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = MediCareCallTheme.colors.bg,
                contentColor = MediCareCallTheme.colors.main,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabIndex])
                            .height(2.dp),
                        color = MediCareCallTheme.colors.main,
                    )
                },
                divider = {},
            ) {
                tabs.forEachIndexed { i, title ->
                    Tab(
                        selected = (i == tabIndex),
                        onClick = {
                            tabIndex = i
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (i == tabIndex)
                                    MediCareCallTheme.colors.main
                                else
                                    MediCareCallTheme.colors.gray2,
                            )
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            key(tabIndex) {
                if (tabIndex == 0) {
                    FirstTimeWheelPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(206.dp),
                        initialHour = firstHour,
                        initialMinute = firstMinute,
                    ) { hour, minute ->
                        firstHour = hour; firstMinute = minute
                    }
                } else if (tabIndex == 1) {
                    SecondTimeWheelPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(206.dp),
                        initialHour = secondHour,
                        initialMinute = secondMinute,
                    ) { hour, minute ->
                        secondHour = hour; secondMinute = minute
                    }
                } else if (tabIndex == 2) {
                    ThirdTimeWheelPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(206.dp),
                        initialHour = thirdHour,
                        initialMinute = thirdMinute,
                    ) { hour, minute ->
                        thirdHour = hour; thirdMinute = minute
                    }
                }
            }
            Spacer(modifier = Modifier.height(38.dp))
            if (tabIndex == 0) {
                CTAButton(
                    CTAButtonType.GREEN,
                    "다음",
                    onClick = {
                        tabIndex = 1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                        ),
                )
            } else if (tabIndex == 1) {
                CTAButton(
                    CTAButtonType.GREEN,
                    "다음",
                    onClick = {
                        tabIndex = 2
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                        ),
                )
            } else if (tabIndex == 2) {
                CTAButton(
                    CTAButtonType.GREEN,
                    "확인",
                    onClick = {
                        onConfirm(
                            firstHour,
                            firstMinute,
                            secondHour,
                            secondMinute,
                            thirdHour,
                            thirdMinute,
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimePickerBottomSheetPreview() {
    MediCareCallTheme {
        TimePickerBottomSheet(
            visible = true,
            onDismiss = {},
            onConfirm = { _, _, _, _, _, _ -> },
        )
    }
}
