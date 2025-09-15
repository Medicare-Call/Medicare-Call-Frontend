package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.data.api.ElderRegisterService
import com.konkuk.medicarecall.data.api.EldersInfoService
import com.konkuk.medicarecall.data.api.MemberRegisterService
import com.konkuk.medicarecall.data.api.NaverPayService
import com.konkuk.medicarecall.data.api.NoticeService
import com.konkuk.medicarecall.data.api.SetCallService
import com.konkuk.medicarecall.data.api.SettingService
import com.konkuk.medicarecall.data.api.SubscribeService
import com.konkuk.medicarecall.data.api.VerificationService
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
}
