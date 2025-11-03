package com.konkuk.medicarecall.ui.feature.calendar.viewmodel

import java.time.LocalDate

data class CalendarUiState(
    val currentYear: Int, // 현재 연도
    val currentMonth: Int, // 현재 월
    val weekDates: List<LocalDate>, // 해당 주의 날짜 리스트
    val selectedDate: LocalDate, // 선택된 날짜 (1일~31일 아님, 실제 날짜)
)
