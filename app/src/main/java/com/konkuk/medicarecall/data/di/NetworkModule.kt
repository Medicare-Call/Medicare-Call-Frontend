package com.konkuk.medicarecall.data.di

import android.util.Log
import com.konkuk.medicarecall.BuildConfig
import com.konkuk.medicarecall.data.api.TokenRefreshService
import com.konkuk.medicarecall.data.network.AuthAuthenticator
import com.konkuk.medicarecall.data.network.AuthInterceptor
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(dataStoreRepository: DataStoreRepository): Interceptor {
        return AuthInterceptor(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideAuthAuthenticator(
        dataStoreRepository: DataStoreRepository,
        tokenRefreshService: dagger.Lazy<TokenRefreshService>
    ): AuthAuthenticator {
        return AuthAuthenticator(dataStoreRepository, tokenRefreshService)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        Log.d("Retrofit", "Base URL: ${BuildConfig.BASE_URL}")
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }
}
