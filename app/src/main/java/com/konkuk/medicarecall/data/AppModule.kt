package com.konkuk.medicarecall.data

import com.konkuk.medicarecall.data.api.TokenRefreshService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenRefreshService(retrofit: Retrofit): TokenRefreshService {
        return retrofit.create(TokenRefreshService::class.java)
    }

}
