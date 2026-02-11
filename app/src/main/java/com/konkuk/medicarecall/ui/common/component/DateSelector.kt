package com.konkuk.medicarecall.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onMonthClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    onMonthClick // TODO: 현재 사용되지 않는 변수입니다. 추후 기능 추가 시 활용할 수 있습니다.
    var showDatePicker by remember { mutableStateOf(false) }

    val year = selectedDate.year
    val month = selectedDate.month.number

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = null,
            ) { showDatePicker = true },

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${year}년 ${month}월",
            style = MediCareCallTheme.typography.SB_20,
            color = MediCareCallTheme.colors.gray9,
            modifier = Modifier.padding(end = 4.dp),
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_down_small),
            contentDescription = "Month Select",
            tint = MediCareCallTheme.colors.gray4,
        )
    }

    // 모달 표시 조건
    if (showDatePicker) {
        DatePickerModal(
            initialDate = selectedDate,
            onDateSelected = {
                onDateSelected(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelector() {
    val fakeDate = LocalDate(2025, 7, 22)
    DateSelector(
        selectedDate = fakeDate,
        onMonthClick = { },
        onDateSelected = { /* no-op */ },
    )
}
