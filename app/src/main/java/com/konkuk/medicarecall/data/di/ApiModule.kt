package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.api.elders.GlucoseService
import com.konkuk.medicarecall.data.api.elders.HealthService
import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.api.elders.MedicineService
import com.konkuk.medicarecall.data.api.elders.MentalService
import com.konkuk.medicarecall.data.api.elders.SetCallService
import com.konkuk.medicarecall.data.api.elders.SleepService
import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.api.elders.SubscribeService
import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.api.payments.NaverPayService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRefreshService(@Named("AuthRetrofit") retrofit: Retrofit): RefreshService =
        retrofit.create(RefreshService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideEldersInfoService(retrofit: Retrofit): EldersInfoService {
        return retrofit.create(EldersInfoService::class.java)
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
