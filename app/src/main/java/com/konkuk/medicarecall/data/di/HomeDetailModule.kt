package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel.GlucoseViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeDetailModule = module {

    viewModel {
        GlucoseViewModel(
            get(),
        )
    }

    viewModel {
        MealViewModel(
            get(),
        )
    }

    viewModel {
        MedicineViewModel(
            get(),
        )
    }

    viewModel {
        SleepViewModel(
            get(),
        )
    }

    viewModel {
        HealthViewModel(
            get(),
        )
    }

    viewModel {
        MentalViewModel(
            get(),
        )
    }
}
