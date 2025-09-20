package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.ElderRegisterService
import com.konkuk.medicarecall.data.api.EldersInfoService
import com.konkuk.medicarecall.data.api.MemberRegisterService
import com.konkuk.medicarecall.data.api.NaverPayService
import com.konkuk.medicarecall.data.api.NoticeService
import com.konkuk.medicarecall.data.api.SetCallService
import com.konkuk.medicarecall.data.api.SettingService
import com.konkuk.medicarecall.data.api.SubscribeService
import com.konkuk.medicarecall.data.api.TokenRefreshService
import com.konkuk.medicarecall.data.api.VerificationService
import com.konkuk.medicarecall.ui.feature.home.data.HomeService
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.data.GlucoseService
import com.konkuk.medicarecall.ui.feature.homedetail.meal.data.MealService
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.data.MedicineService
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.data.SleepService
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.data.HealthService
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.data.MentalService
import com.konkuk.medicarecall.ui.feature.statistics.data.StatisticsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideEldersInfoService(retrofit: Retrofit): EldersInfoService {
        return retrofit.create(EldersInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideVerificationService(retrofit: Retrofit): VerificationService {
        return retrofit.create(VerificationService::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberRegisterService(retrofit: Retrofit): MemberRegisterService {
        return retrofit.create(MemberRegisterService::class.java)
    }

    @Provides
    @Singleton
    fun provideElderRegisterService(retrofit: Retrofit): ElderRegisterService {
        return retrofit.create(ElderRegisterService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeService(retrofit: Retrofit): NoticeService {
        return retrofit.create(NoticeService::class.java)
    }

    @Provides
    @Singleton
    fun provideSetCallService(retrofit: Retrofit): SetCallService {
        return retrofit.create(SetCallService::class.java)
    }

    @Provides
    @Singleton
    fun provideSubscribeService(retrofit: Retrofit): SubscribeService {
        return retrofit.create(SubscribeService::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingService(retrofit: Retrofit): SettingService {
        return retrofit.create(SettingService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverPayService(retrofit: Retrofit): NaverPayService {
        return retrofit.create(NaverPayService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenRefreshService(retrofit: Retrofit): TokenRefreshService {
        return retrofit.create(TokenRefreshService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Provides
    @Singleton
    fun provideGlucoseService(retrofit: Retrofit): GlucoseService {
        return retrofit.create(GlucoseService::class.java)
    }

    @Provides
    @Singleton
    fun provideMealService(retrofit: Retrofit): MealService {
        return retrofit.create(MealService::class.java)
    }

    @Provides
    @Singleton
    fun provideMedicineService(retrofit: Retrofit): MedicineService {
        return retrofit.create(MedicineService::class.java)
    }

    @Provides
    @Singleton
    fun provideSleepService(retrofit: Retrofit): SleepService {
        return retrofit.create(SleepService::class.java)
    }

    @Provides
    @Singleton
    fun provideHealthService(retrofit: Retrofit): HealthService {
        return retrofit.create(HealthService::class.java)
    }

    @Provides
    @Singleton
    fun provideMentalService(retrofit: Retrofit): MentalService {
        return retrofit.create(MentalService::class.java)
    }

    @Provides
    @Singleton
    fun provideStatisticsService(retrofit: Retrofit): StatisticsService {
        return retrofit.create(StatisticsService::class.java)
    }
}
