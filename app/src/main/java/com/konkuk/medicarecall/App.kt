package com.konkuk.medicarecall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.repository.FcmRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        logFcmToken()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return

        val channel = NotificationChannel(
            FCM_CHANNEL_ID,
            "FCM 알림명",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Firebase Cloud Messaging으로부터 수신된 알림을 표시합니다."
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 150, 250)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        nm.createNotificationChannel(channel)
    }

    private fun logFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "토큰 가져오기 실패", task.exception)
                    }
                    return@addOnCompleteListener
                }

                val token = task.result
                if (BuildConfig.DEBUG) {
                    val masked = token.take(8) + "…" + token.takeLast(4)
                    Log.d(TAG, "FCM token(debug)=$masked")
                    Log.d(TAG, "FCM token(full)=$token")
                }

                // FCM 토큰을 DataStore(AppPreferences)에 저장
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        fcmRepository.saveFcmToken(token)
                        Log.d(TAG, "FCM token saved to DataStore")
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
