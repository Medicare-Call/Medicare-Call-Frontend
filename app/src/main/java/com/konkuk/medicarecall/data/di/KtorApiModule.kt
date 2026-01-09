package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.GlucoseService
import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.api.elders.MedicineService
import com.konkuk.medicarecall.data.api.elders.MentalService
import com.konkuk.medicarecall.data.api.elders.SleepService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class KtorApiModule {

    @Single
    fun homeService(ktorfit: Ktorfit): HomeService =
        ktorfit.create()

    @Single
    fun glucoseService(ktorfit: Ktorfit): GlucoseService =
        ktorfit.create()

    @Single
    fun mealService(ktorfit: Ktorfit): MealService =
        ktorfit.create()

    @Single
    fun medicineService(ktorfit: Ktorfit): MedicineService =
        ktorfit.create()

    @Single
    fun mentalService(ktorfit: Ktorfit): MentalService =
        ktorfit.create()

    @Single
    fun sleepService(ktorfit: Ktorfit): SleepService =
        ktorfit.create()
}
