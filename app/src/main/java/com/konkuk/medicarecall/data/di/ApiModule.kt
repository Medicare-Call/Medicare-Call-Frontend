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
import com.konkuk.medicarecall.ui.home.data.HomeApi
import com.konkuk.medicarecall.ui.homedetail.glucoselevel.data.GlucoseApi
import com.konkuk.medicarecall.ui.homedetail.meal.data.MealApi
import com.konkuk.medicarecall.ui.homedetail.medicine.data.MedicineApi
import com.konkuk.medicarecall.ui.homedetail.sleep.data.SleepApi
import com.konkuk.medicarecall.ui.homedetail.statehealth.data.HealthApi
import com.konkuk.medicarecall.ui.homedetail.statemental.data.MentalApi
import com.konkuk.medicarecall.ui.statistics.data.StatisticsApi
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
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGlucoseApi(retrofit: Retrofit): GlucoseApi {
        return retrofit.create(GlucoseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMealApi(retrofit: Retrofit): MealApi {
        return retrofit.create(MealApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMedicineApi(retrofit: Retrofit): MedicineApi {
        return retrofit.create(MedicineApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSleepApi(retrofit: Retrofit): SleepApi {
        return retrofit.create(SleepApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHealthApi(retrofit: Retrofit): HealthApi {
        return retrofit.create(HealthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMentalApi(retrofit: Retrofit): MentalApi {
        return retrofit.create(MentalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStatisticsApi(retrofit: Retrofit): StatisticsApi {
        return retrofit.create(StatisticsApi::class.java)
    }
}
