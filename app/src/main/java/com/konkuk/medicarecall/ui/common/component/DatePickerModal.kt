package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val dpColors = DatePickerDefaults.colors(
        containerColor = Color.Companion.White,
        // 👇 상속받지 말고 명시적으로 지정
        titleContentColor = MediCareCallTheme.colors.black,
        headlineContentColor = MediCareCallTheme.colors.black,
        weekdayContentColor = MediCareCallTheme.colors.gray7,
        subheadContentColor = MediCareCallTheme.colors.gray7,
        dayContentColor = MediCareCallTheme.colors.black,
        disabledDayContentColor = MediCareCallTheme.colors.black.copy(alpha = 0.38f),

        // 선택/오늘 강조
        selectedDayContainerColor = MediCareCallTheme.colors.main,
        selectedDayContentColor = Color.Companion.White,
        todayContentColor = MediCareCallTheme.colors.black,
        todayDateBorderColor = MediCareCallTheme.colors.main,
        selectedYearContentColor = MediCareCallTheme.colors.white,
    )

    // 세계 표준시
    val utc = ZoneOffset.UTC

    val initialMillis = remember(initialDate) {
        initialDate.atStartOfDay(utc).toInstant().toEpochMilli()
    }
    val initialMonthMillis = remember(initialDate) {
        initialDate.withDayOfMonth(1).atStartOfDay(utc).toInstant().toEpochMilli()
    }

    // initialDate가 바뀌면 상태 재생성
    key(initialMillis) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis,
            initialDisplayedMonthMillis = initialMonthMillis,
        )

        val confirmDate = datePickerState.selectedDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(utc).toLocalDate()
        }

        DatePickerDialog(
            onDismissRequest = onDismiss,
            colors = DatePickerDefaults.colors(
                containerColor = MediCareCallTheme.colors.white,
            ),
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MediCareCallTheme.colors.black,
                    ),
                    onClick = {
                        confirmDate?.let(onDateSelected)
                        onDismiss()
                    },
                ) { Text("확인") }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MediCareCallTheme.colors.black,
                    ),
                    onClick = onDismiss,
                ) { Text("취소") }
            },
        ) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.Companion.White) {
                DatePicker(state = datePickerState, colors = dpColors)
            }
        }
    }
}

@Preview
@Composable
fun PreviewDatePickerModal() {
    DatePickerModal(
        initialDate = LocalDate.now(),
        onDateSelected = {},
        onDismiss = {},
    )
}
