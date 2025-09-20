package com.konkuk.medicarecall.data.di

import android.content.Context
import com.konkuk.medicarecall.data.api.ElderRegisterService
import com.konkuk.medicarecall.data.api.EldersInfoService
import com.konkuk.medicarecall.data.api.MemberRegisterService
import com.konkuk.medicarecall.data.api.NaverPayService
import com.konkuk.medicarecall.data.api.NoticeService
import com.konkuk.medicarecall.data.api.SetCallService
import com.konkuk.medicarecall.data.api.SettingService
import com.konkuk.medicarecall.data.api.SubscribeService
import com.konkuk.medicarecall.data.api.VerificationService
import com.konkuk.medicarecall.data.repositoryimpl.DataStoreRepository
import com.konkuk.medicarecall.data.repositoryimpl.ElderIdRepository
import com.konkuk.medicarecall.data.repositoryimpl.ElderRegisterRepository
import com.konkuk.medicarecall.data.repositoryimpl.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repositoryimpl.EldersInfoRepository
import com.konkuk.medicarecall.data.repositoryimpl.MemberRegisterRepository
import com.konkuk.medicarecall.data.repositoryimpl.NaverPayRepository
import com.konkuk.medicarecall.data.repositoryimpl.NoticeRepository
import com.konkuk.medicarecall.data.repositoryimpl.SetCallRepository
import com.konkuk.medicarecall.data.repositoryimpl.SubscribeRepository
import com.konkuk.medicarecall.data.repositoryimpl.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.repositoryimpl.UserRepository
import com.konkuk.medicarecall.data.repositoryimpl.VerificationRepository
import com.konkuk.medicarecall.data.repositoryimpl.DataStoreRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderIdRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersHealthInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.MemberRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NaverPayRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NoticeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SetCallRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SubscribeRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.UpdateElderInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.UserRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.VerificationRepositoryImpl
import com.konkuk.medicarecall.ui.feature.home.data.HomeRepository
import com.konkuk.medicarecall.ui.feature.home.data.HomeRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.data.GlucoseRepository
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.data.GlucoseRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.meal.data.MealRepository
import com.konkuk.medicarecall.ui.feature.homedetail.meal.data.MealRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.data.MedicineRepository
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.data.MedicineRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.data.SleepRepository
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.data.SleepRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.data.HealthRepository
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.data.HealthRepositoryImpl
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.data.MentalRepository
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.data.MentalRepositoryImpl
import com.konkuk.medicarecall.ui.feature.statistics.data.StatisticsRepository
import com.konkuk.medicarecall.ui.feature.statistics.data.StatisticsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepository(context)
    }

    @Provides
    @Singleton
    fun provideEldersInfoRepository(service: EldersInfoService): EldersInfoRepository {
        return EldersInfoRepository(service)
    }

    @Provides
    @Singleton
    fun provideVerificationRepository(service: VerificationService): VerificationRepository {
        return VerificationRepository(service)
    }

    @Provides
    @Singleton
    fun provideMemberRegisterRepository(service: MemberRegisterService): MemberRegisterRepository {
        return MemberRegisterRepository(service)
    }

    @Provides
    @Singleton
    fun provideNoticeRepository(service: NoticeService): NoticeRepository {
        return NoticeRepository(service)
    }

    @Provides
    @Singleton
    fun provideSetCallRepository(service: SetCallService): SetCallRepository {
        return SetCallRepository(service)
    }

    @Provides
    @Singleton
    fun provideSubscribeRepository(service: SubscribeService): SubscribeRepository {
        return SubscribeRepository(service)
    }

    @Provides
    @Singleton
    fun provideEldersHealthInfoRepository(
        elderInfoService: EldersInfoService,
        elderRegisterService: ElderRegisterService
    ): EldersHealthInfoRepository {
        return EldersHealthInfoRepository(elderInfoService, elderRegisterService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        settingService: SettingService,
        dataStoreRepository: DataStoreRepository
    ): UserRepository {
        return UserRepository(settingService, dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateElderRepository(eldersInfoService: EldersInfoService): UpdateElderInfoRepository {
        return UpdateElderInfoRepository(eldersInfoService)
    }

    @Provides
    @Singleton
    fun provideNaverPayRepository(naverPayService: NaverPayService): NaverPayRepository {
        return NaverPayRepository(naverPayService)
    }
}
