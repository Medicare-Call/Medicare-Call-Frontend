package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.BuildConfig
import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.api.elders.SetCallService
import com.konkuk.medicarecall.data.api.fcm.FcmUpdateService
import com.konkuk.medicarecall.data.api.fcm.FcmValidationService
import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.api.payments.NaverPayService
import com.konkuk.medicarecall.data.network.AuthAuthenticator
import com.konkuk.medicarecall.data.network.AuthInterceptor
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.FcmRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.data.repositoryimpl.DataStoreRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderIdRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.ElderRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.EldersInfoRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.FcmRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.MemberRegisterRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.NaverPayRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.SetCallRepositoryImpl
import com.konkuk.medicarecall.data.repositoryimpl.VerificationRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { AuthInterceptor(get()) }

    single { AuthAuthenticator(get(), get()) }

    single(named("AuthOkHttpClient")) {
        OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .authenticator(get<AuthAuthenticator>())
            .build()
    }

    single(named("AuthRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .client(get(named("AuthOkHttpClient")))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .client(get())
            .build()
    }
}

val apiModule = module {
    single { get<Retrofit>(named("AuthRetrofit")).create(RefreshService::class.java) }
    single { get<Retrofit>().create(AuthService::class.java) }
    single { get<Retrofit>().create(EldersInfoService::class.java) }
    single { get<Retrofit>().create(MemberRegisterService::class.java) }
    single { get<Retrofit>().create(ElderRegisterService::class.java) }
    single { get<Retrofit>().create(SetCallService::class.java) }
    single { get<Retrofit>().create(NaverPayService::class.java) }
    single { get<Retrofit>().create(FcmValidationService::class.java) }
    single { get<Retrofit>().create(FcmUpdateService::class.java) }
}

val repositoryModule = module {
    single<DataStoreRepository> { DataStoreRepositoryImpl(androidContext()) }
    single<VerificationRepository> { VerificationRepositoryImpl(get()) }
    single<MemberRegisterRepository> { MemberRegisterRepositoryImpl(get()) }
    single<EldersInfoRepository> { EldersInfoRepositoryImpl(get()) }
    single<ElderRegisterRepository> { ElderRegisterRepositoryImpl(get()) }
    single<ElderIdRepository> { ElderIdRepositoryImpl() }
    single<SetCallRepository> { SetCallRepositoryImpl(get()) }
    single<NaverPayRepository> { NaverPayRepositoryImpl(get()) }
    single<FcmRepository> { FcmRepositoryImpl(androidContext(), get(), get()) }
}

val appModules = listOf(networkModule, apiModule, repositoryModule)
