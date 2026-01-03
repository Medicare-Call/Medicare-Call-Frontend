package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.ui.feature.calendar.viewmodel.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {

    viewModel {
        CalendarViewModel()
    }
}
