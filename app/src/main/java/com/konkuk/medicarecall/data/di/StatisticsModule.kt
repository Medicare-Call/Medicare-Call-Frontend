package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.data.repositoryimpl.StatisticsRepositoryImpl
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

// 1. 통계 관련 API 모듈
val statisticsApiModule = module {
    single { get<Retrofit>().create(StatisticsService::class.java) }
}

// 2. 통계 관련 레포지토리 모듈
val statisticsRepositoryModule = module {
    // get()을 통해 StatisticsService와 EldersHealthInfoRepository를 자동으로 주입받습니다.
    single<StatisticsRepository> { StatisticsRepositoryImpl(get(), get()) }
}

// 전체 통계 모듈 리스트
val statisticsModules = listOf(
    statisticsApiModule,
    statisticsRepositoryModule,
)
