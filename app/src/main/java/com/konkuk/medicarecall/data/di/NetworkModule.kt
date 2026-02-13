package com.konkuk.medicarecall.data.di

import com.konkuk.medicarecall.BuildConfig.BASE_URL
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Named
annotation class AuthKtorfit

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

@Module
class NetworkModule {

    private val defaultHttpClient: HttpClientConfig<*>.() -> Unit = {
        install(DefaultRequest) {
            url(BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }

    @Single
    fun provideHttpClient(
        refreshService: RefreshService,
        dataStoreRepository: DataStoreRepository,
    ) = HttpClient {
        defaultHttpClient()

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = runBlocking { dataStoreRepository.getAccessToken() }
                    val refreshToken = runBlocking { dataStoreRepository.getRefreshToken() }

                    BearerTokens(
                        accessToken = accessToken ?: "",
                        refreshToken = refreshToken ?: "",
                    )
                }

                refreshTokens {
                    synchronized(this) {
                        // 3. 현재 저장된 토큰들을 가져옴
                        val accessToken = runBlocking { dataStoreRepository.getAccessToken() }
                        val refreshToken = runBlocking { dataStoreRepository.getRefreshToken() }

                        require(!refreshToken.isNullOrEmpty())
                        val refreshResponse = runBlocking {
                            refreshService.refreshToken(refreshToken)
                        }

                        // 6. 토큰 갱신 API 호출 (runBlocking 사용)

                        if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                            // 7. 토큰 갱신 성공 시, 새로운 토큰들을 DataStore에 저장
                            val newTokens = refreshResponse.body()!!
                            runBlocking {
                                dataStoreRepository.saveAccessToken(newTokens.accessToken)
                                dataStoreRepository.saveRefreshToken(newTokens.refreshToken)
                            }

                            val accessToken = runBlocking { dataStoreRepository.getAccessToken() }
                            val refreshToken = runBlocking { dataStoreRepository.getRefreshToken() }

                            BearerTokens(accessToken ?: "", refreshToken)
                            //      response.request.newBuilder()
                            //          .header("Authorization", "Bearer ${newTokens.accessToken}")
                            //          .build() 대체
                        } else {
                            // 9. 토큰 갱신 실패 시 (RefreshToken 만료 등), 저장된 토큰 삭제 후 null 반환
                            runBlocking { dataStoreRepository.saveRefreshToken("") }
                            // 여기서도 로그인 화면으로 보내는 로직 추가 가능
                            null
                        }
                    }
                }
            }
        }
    }

    @Single
    fun ktorfit(client: HttpClient) = Ktorfit.Builder()
        .httpClient(client)
        .converterFactories(ResponseConverterFactory())
        .build()

    @Single
    @AuthKtorfit
    fun authKtorfit() =
        Ktorfit.Builder()
            .httpClient(HttpClient { defaultHttpClient() })
            .converterFactories(ResponseConverterFactory())
            .build()
//    @Single
//    fun authInterceptor(dataStoreRepository: DataStoreRepository) = AuthInterceptor(dataStoreRepository)
//
//    @Single
//    fun authAuthenticator(
//        dataStoreRepository: DataStoreRepository,
//        refreshService: RefreshService,
//    ) = AuthAuthenticator(dataStoreRepository, refreshService)
//
//    @Single
//    fun httpLoggingInterceptor() = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    @Single
//    fun okHttpClient(
//        authInterceptor: AuthInterceptor,
//        loggingInterceptor: HttpLoggingInterceptor,
//        authAuthenticator: AuthAuthenticator,
//    ) = OkHttpClient.Builder().apply {
//        readTimeout(20, TimeUnit.SECONDS)
//        addInterceptor(authInterceptor)
//        if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
//        authenticator(authAuthenticator)
//    }.build()
//
//    @Single
//    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
//        .baseUrl(BuildConfig.BASE_URL)
//        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//        .client(okHttpClient)
//        .build()
//
//    @Single
//    @AuthRetrofit
//    fun authRetrofit(loggingInterceptor: HttpLoggingInterceptor): Retrofit {
//        val authOkHttpClient = OkHttpClient.Builder().apply {
//            readTimeout(20, TimeUnit.SECONDS)
//            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
//        }.build()
//
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//            .client(authOkHttpClient)
//            .build()
//    }
}
