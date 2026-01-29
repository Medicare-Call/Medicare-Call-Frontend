package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.api.auth.createAuthService
import com.konkuk.medicarecall.data.api.auth.createRefreshService
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
import com.konkuk.medicarecall.data.api.elders.createElderRegisterService
import com.konkuk.medicarecall.data.api.elders.createEldersInfoService
import com.konkuk.medicarecall.data.api.elders.createGlucoseService
import com.konkuk.medicarecall.data.api.elders.createHealthService
import com.konkuk.medicarecall.data.api.elders.createHomeService
import com.konkuk.medicarecall.data.api.elders.createMealService
import com.konkuk.medicarecall.data.api.elders.createMedicineService
import com.konkuk.medicarecall.data.api.elders.createMentalService
import com.konkuk.medicarecall.data.api.elders.createSetCallService
import com.konkuk.medicarecall.data.api.elders.createSleepService
import com.konkuk.medicarecall.data.api.elders.createStatisticsService
import com.konkuk.medicarecall.data.api.elders.createSubscribeService
import com.konkuk.medicarecall.data.api.fcm.FcmUpdateService
import com.konkuk.medicarecall.data.api.fcm.FcmValidationService
import com.konkuk.medicarecall.data.api.fcm.createFcmUpdateService
import com.konkuk.medicarecall.data.api.fcm.createFcmValidationService
import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.api.member.createMemberRegisterService
import com.konkuk.medicarecall.data.api.member.createSettingService
import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.api.notice.createNoticeService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ApiModule {

    @Single
    fun refreshService(@AuthKtorfit ktorfit: Ktorfit): RefreshService =
        ktorfit.createRefreshService()

    @Single
    fun authService(ktorfit: Ktorfit): AuthService =
        ktorfit.createAuthService()

    @Single
    fun eldersInfoService(ktorfit: Ktorfit): EldersInfoService =
        ktorfit.createEldersInfoService()

    @Single
    fun memberRegisterService(ktorfit: Ktorfit): MemberRegisterService =
        ktorfit.createMemberRegisterService()

    @Single
    fun provideElderRegisterService(ktorfit: Ktorfit): ElderRegisterService =
        ktorfit.createElderRegisterService()

    @Single
    fun provideNoticeService(ktorfit: Ktorfit): NoticeService =
        ktorfit.createNoticeService()

    @Single
    fun provideSetCallService(ktorfit: Ktorfit): SetCallService =
        ktorfit.createSetCallService()

    @Single
    fun provideSubscribeService(ktorfit: Ktorfit): SubscribeService =
        ktorfit.createSubscribeService()

    @Single
    fun provideSettingService(ktorfit: Ktorfit): SettingService =
        ktorfit.createSettingService()

    @Single
    fun provideHomeService(ktorfit: Ktorfit): HomeService =
        ktorfit.createHomeService()

    @Single
    fun provideGlucoseService(ktorfit: Ktorfit): GlucoseService =
        ktorfit.createGlucoseService()

    @Single
    fun provideMealService(ktorfit: Ktorfit): MealService =
        ktorfit.createMealService()

    @Single
    fun provideMedicineService(ktorfit: Ktorfit): MedicineService =
        ktorfit.createMedicineService()

    @Single
    fun provideSleepService(ktorfit: Ktorfit): SleepService =
        ktorfit.createSleepService()

    @Single
    fun provideHealthService(ktorfit: Ktorfit): HealthService =
        ktorfit.createHealthService()

    @Single
    fun provideMentalService(ktorfit: Ktorfit): MentalService =
        ktorfit.createMentalService()

    @Single
    fun provideStatisticsService(ktorfit: Ktorfit): StatisticsService =
        ktorfit.createStatisticsService()

    @Single
    fun provideFcmValidationService(ktorfit: Ktorfit): FcmValidationService =
        ktorfit.createFcmValidationService()

    @Single
    fun provideFcmUpdateService(ktorfit: Ktorfit): FcmUpdateService =
        ktorfit.createFcmUpdateService()
}
