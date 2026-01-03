package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.data.repositoryimpl.StatisticsRepositoryImpl
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.StatisticsViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val statisticsModule = module {
    // API Service
    single { get<Retrofit>().create(StatisticsService::class.java) }


    // Repository
    single<StatisticsRepository> { StatisticsRepositoryImpl(get<StatisticsService>(), get()) }

    // ViewModel
    viewModel { StatisticsViewModel(get(), get()) }
}

@Module
class StatisticsModule {

    @Single
    fun statisticsService(retrofit: Retrofit) = retrofit.create<StatisticsService>()

    @Single
    fun statisticsRepository(
        statisticsService: StatisticsService,
        elderHealthInfoRepository: EldersHealthInfoRepository,
    ) = StatisticsRepositoryImpl(statisticsService, elderHealthInfoRepository)

//    @KoinViewModel
//    fun statisticsViewModel(
//        statisticsRepository: StatisticsRepository,
//        elderHealthInfoRepository: EldersHealthInfoRepository,
//    ) = StatisticsViewModel(statisticsRepository, elderHealthInfoRepository)
}
