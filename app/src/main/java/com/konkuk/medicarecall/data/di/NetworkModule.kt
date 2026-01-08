package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.BuildConfig
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.network.AuthAuthenticator
import com.konkuk.medicarecall.data.network.AuthInterceptor
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@Named
annotation class AuthRetrofit

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

@Module
class NetworkModule {

    @Single
    fun authInterceptor(dataStoreRepository: DataStoreRepository) = AuthInterceptor(dataStoreRepository)

    @Single
    fun authAuthenticator(
        dataStoreRepository: DataStoreRepository,
        refreshService: RefreshService,
    ) = AuthAuthenticator(dataStoreRepository, refreshService)

    @Single
    fun httpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Single
    fun okHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        authAuthenticator: AuthAuthenticator,
    ) = OkHttpClient.Builder().apply {
        readTimeout(20, TimeUnit.SECONDS)
        addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        authenticator(authAuthenticator)
    }.build()

    @Single
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    @Single
    @AuthRetrofit
    fun authRetrofit(loggingInterceptor: HttpLoggingInterceptor): Retrofit {
        val authOkHttpClient = OkHttpClient.Builder().apply {
            readTimeout(20, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(authOkHttpClient)
            .build()
    }
}
