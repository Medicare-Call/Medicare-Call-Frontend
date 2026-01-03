package com.konkuk.medicarecall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.di.appModules
import com.konkuk.medicarecall.data.di.statisticsModule
import com.konkuk.medicarecall.data.di.settingsModules
import com.konkuk.medicarecall.data.di.calendarModule
import com.konkuk.medicarecall.data.di.homeDetailModule
import com.konkuk.medicarecall.data.di.homeModule
import com.konkuk.medicarecall.data.repository.FcmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

class App : Application(), KoinComponent {

    private val fcmRepository: FcmRepository by inject()

    // Application 전체에서 쑬 수 있는 스코프(앱이 살아있는 동안 유지돼야 하는 초기화/저장 작업 진행 - 여러 초기화 작업 한덩어리로 관리)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                settingsModules +
                    calendarModule +
                    homeModule +
                    homeDetailModule +
                    statisticsModule +
                    appModules +
                    defaultModule,
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
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "토큰 가져오기 실패", task.exception)
                    }
                    return@addOnCompleteListener
                }

                val token = task.result
                if (token.isNullOrBlank()) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "FCM Token is empty")
                    }
                    return@addOnCompleteListener
                }
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "FCM token(full)=$token")
                }

                // FCM 토큰을 DataStore(AppPreferences)에 저장
                appScope.launch {
                    try {
                        fcmRepository.saveFcmToken(token)
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "FCM token saved to DataStore")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save FCM token", e)
                    }
                }
            }
    }

    companion object {
        private const val TAG = "FCM"
        const val FCM_CHANNEL_ID = "fcm_alert"
    }
}
