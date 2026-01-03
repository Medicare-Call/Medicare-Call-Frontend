package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.api.elders.SubscribeService
import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.NoticeRepository
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.repositoryimpl.ElderIdRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersHealthInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NoticeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SubscribeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.UpdateElderInfoRepositoryImpl
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailElderInfoViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailHealthViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.DetailMyDataViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersHealthViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.EldersInfoViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.MyDataViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.NoticeViewModel
import com.konkuk.medicarecall.ui.feature.settings.viewmodel.SubscribeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

// 설정 관련 API 서비스 모듈
val settingsApiModule = module {
    single { get<Retrofit>().create(NoticeService::class.java) }
    single { get<Retrofit>().create(EldersInfoService::class.java) }
    single { get<Retrofit>().create(ElderRegisterService::class.java) }
    single { get<Retrofit>().create(SubscribeService::class.java) }
}

// 설정 관련 레포지토리 모듈
val settingsRepositoryModule = module {
    single<NoticeRepository> { NoticeRepositoryImpl(get<NoticeService>()) }
    single<SubscribeRepository> { SubscribeRepositoryImpl(get<SubscribeService>()) }
    single<UpdateElderInfoRepository> { UpdateElderInfoRepositoryImpl(get<EldersInfoService>()) }
    single<EldersInfoRepository> { EldersInfoRepositoryImpl(get<EldersInfoService>()) }
    single<EldersHealthInfoRepository> { EldersHealthInfoRepositoryImpl(get<EldersInfoService>(), get<ElderRegisterService>()) }

    single<ElderIdRepository> { ElderIdRepositoryImpl() }
}

// 설정 관련 뷰모델 모듈
val settingsViewModelModule = module {
    viewModel { NoticeViewModel(get()) }
    viewModel { SubscribeViewModel(get()) }
    viewModel { MyDataViewModel(get()) }
    viewModel { EldersInfoViewModel(get(), get()) }
    viewModel { EldersHealthViewModel(get()) }

    viewModel { DetailElderInfoViewModel(get()) }
    viewModel { DetailHealthViewModel(get()) }
    viewModel { DetailMyDataViewModel(get()) }
}

// 설정 도메인 전체 모듈 결합
val settingsModules = listOf(
    settingsApiModule,
    settingsRepositoryModule,
    settingsViewModelModule,
)
