package com.konkuk.medicarecall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.di.ApiModule
import com.konkuk.medicarecall.data.di.LocalModule
import com.konkuk.medicarecall.data.di.NetworkModule
import com.konkuk.medicarecall.data.repository.FcmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

@KoinApplication
class App : Application() {

    private val fcmRepository: FcmRepository by inject()

    // Application 전체에서 쑬 수 있는 스코프(앱이 살아있는 동안 유지돼야 하는 초기화/저장 작업 진행 - 여러 초기화 작업 한덩어리로 관리)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                defaultModule,
                ApiModule().module,
                NetworkModule().module,
                LocalModule().module,
            )
        }
        createNotificationChannel()
        fetchAndStoreFcmToken()
    }

    // Android 8.0 이상에서 FCM 알림 표시하기 위한 채널 생성
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return

        val channel = NotificationChannel(
            FCM_CHANNEL_ID,
            "FCM 알림",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Firebase Cloud Messaging으로부터 수신된 알림을 표시합니다."
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 150, 250)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        nm.createNotificationChannel(channel)
    }

    /**
     * 앱 시작 시점에 한 번 FCM 토큰을 가져와서 DataStore에 저장
     * (실제로 토큰이 바뀌면 FirebaseMessagingService.onNewToken(...)에서도 다시 저장해야 함)
     */
    private fun fetchAndStoreFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                val token = task.result
                if (token.isNullOrBlank()) {
                    return@addOnCompleteListener
                }

                // FCM 토큰을 DataStore(AppPreferences)에 저장
                appScope.launch {
                    try {
                        fcmRepository.saveFcmToken(token)
                    } catch (e: Exception) {
                        // FCM 토큰 저장 실패 무시
                    }
                }
            }
    }

    companion object {
        const val FCM_CHANNEL_ID = "fcm_alert"
    }
}
