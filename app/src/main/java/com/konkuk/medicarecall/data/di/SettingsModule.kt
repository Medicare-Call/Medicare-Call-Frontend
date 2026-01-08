package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.*
import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.repository.*
import com.konkuk.medicarecall.data.repositoryimpl.*
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

// 설정 관련 API 모듈
val settingsApiModule = module {
    single { get<Retrofit>().create(NoticeService::class.java) }
    single { get<Retrofit>().create(EldersInfoService::class.java) }
    single { get<Retrofit>().create(ElderRegisterService::class.java) }
    single { get<Retrofit>().create(SubscribeService::class.java) }
}

// 설정 관련 레포지토리 모듈
val settingsRepositoryModule = module {
    single<NoticeRepository> { NoticeRepositoryImpl(get()) }
    single<SubscribeRepository> { SubscribeRepositoryImpl(get()) }
    single<UpdateElderInfoRepository> { UpdateElderInfoRepositoryImpl(get()) }
    single<EldersInfoRepository> { EldersInfoRepositoryImpl(get()) }
    single<EldersHealthInfoRepository> { EldersHealthInfoRepositoryImpl(get(), get()) }
    single<ElderIdRepository> { ElderIdRepositoryImpl() }
}

// 전체 설정 모듈 리스트
val settingsModules = listOf(
    settingsApiModule,
    settingsRepositoryModule,
)
