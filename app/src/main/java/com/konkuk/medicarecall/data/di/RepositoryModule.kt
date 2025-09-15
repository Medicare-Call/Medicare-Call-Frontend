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
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import com.konkuk.medicarecall.data.repository.NoticeRepository
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.ui.homedetail.meal.data.MealApi
import com.konkuk.medicarecall.ui.homedetail.meal.data.MealRepository
import com.konkuk.medicarecall.ui.homedetail.meal.data.MealRepositoryImpl
import com.konkuk.medicarecall.ui.homedetail.medicine.data.MedicineApi
import com.konkuk.medicarecall.ui.homedetail.medicine.data.MedicineRepository
import com.konkuk.medicarecall.ui.homedetail.medicine.data.MedicineRepositoryImpl
import com.konkuk.medicarecall.ui.statistics.data.StatisticsRepository
import com.konkuk.medicarecall.ui.statistics.data.StatisticsRepositoryImpl
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

    @Provides
    @Singleton
    fun provideMealRepository(mealApi: MealApi): MealRepository {
        return MealRepositoryImpl(mealApi)
    }

    @Provides
    @Singleton
    fun provideMedicineRepository(
        medicineApi: MedicineApi,
        eldersHealthInfoRepository: EldersHealthInfoRepository
    ): MedicineRepository {
        return MedicineRepositoryImpl(medicineApi, eldersHealthInfoRepository)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticsRepository
}
