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
import com.konkuk.medicarecall.data.api.fcm.FcmUpdateService
import com.konkuk.medicarecall.data.api.fcm.FcmValidationService
import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.api.payments.NaverPayService
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

@Module
class ApiModule {

    @Single
    fun refreshService(@AuthRetrofit retrofit: Retrofit): RefreshService =
        retrofit.create(RefreshService::class.java)

    @Single
    fun authService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Single
    fun eldersInfoService(retrofit: Retrofit): EldersInfoService {
        return retrofit.create(EldersInfoService::class.java)
    }

    @Single
    fun memberRegisterService(retrofit: Retrofit): MemberRegisterService {
        return retrofit.create(MemberRegisterService::class.java)
    }

    @Single
    fun provideElderRegisterService(retrofit: Retrofit): ElderRegisterService {
        return retrofit.create(ElderRegisterService::class.java)
    }

    @Single
    fun provideNoticeService(retrofit: Retrofit): NoticeService {
        return retrofit.create(NoticeService::class.java)
    }

    @Single
    fun provideSetCallService(retrofit: Retrofit): SetCallService {
        return retrofit.create(SetCallService::class.java)
    }

    @Single
    fun provideSubscribeService(retrofit: Retrofit): SubscribeService {
        return retrofit.create(SubscribeService::class.java)
    }

    @Single
    fun provideSettingService(retrofit: Retrofit): SettingService {
        return retrofit.create(SettingService::class.java)
    }

    @Single
    fun provideNaverPayService(retrofit: Retrofit): NaverPayService {
        return retrofit.create(NaverPayService::class.java)
    }

    @Single
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Single
    fun provideGlucoseService(retrofit: Retrofit): GlucoseService {
        return retrofit.create(GlucoseService::class.java)
    }

    @Single
    fun provideMealService(retrofit: Retrofit): MealService {
        return retrofit.create(MealService::class.java)
    }

    @Single
    fun provideMedicineService(retrofit: Retrofit): MedicineService {
        return retrofit.create(MedicineService::class.java)
    }

    @Single
    fun provideSleepService(retrofit: Retrofit): SleepService {
        return retrofit.create(SleepService::class.java)
    }

    @Single
    fun provideHealthService(retrofit: Retrofit): HealthService {
        return retrofit.create(HealthService::class.java)
    }

    @Single
    fun provideMentalService(retrofit: Retrofit): MentalService {
        return retrofit.create(MentalService::class.java)
    }

    @Single
    fun provideStatisticsService(retrofit: Retrofit): StatisticsService {
        return retrofit.create(StatisticsService::class.java)
    }

    @Single
    fun provideFcmValidationService(retrofit: Retrofit): FcmValidationService {
        return retrofit.create(FcmValidationService::class.java)
    }

    @Single
    fun provideFcmUpdateService(retrofit: Retrofit): FcmUpdateService {
        return retrofit.create(FcmUpdateService::class.java)
    }
}
