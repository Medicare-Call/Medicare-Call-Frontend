package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.data.repositoryimpl.StatisticsRepositoryImpl
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val statisticsModule = module {
    // API Service
    single { get<Retrofit>().create(StatisticsService::class.java) }

    // Repository
    single<StatisticsRepository> { StatisticsRepositoryImpl(get<StatisticsService>(), get<EldersHealthInfoRepository>()) }

    // ViewModel
    viewModel { StatisticsViewModel(get(), get()) }
}

