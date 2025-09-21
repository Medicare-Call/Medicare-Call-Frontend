package com.konkuk.medicarecall

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        logFcmToken()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return

        val channel = NotificationChannel(
            FCM_CHANNEL_ID,                                        //  새 채널 ID
            "FCM 알림명",
            NotificationManager.IMPORTANCE_HIGH,                    // Heads-up 가능
        ).apply {
            description = "Firebase Cloud Messaging으로부터 수신된 알림을 표시합니다."
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 150, 250)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC  // 잠금화면에 내용 표시
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
                    Log.d(TAG, "FCM token(full)=$token") // 여기에서 서버에 토큰 넘겨주면 됨
                } // 콘솔 테스트 시 복사해서 사용
                // TODO: 필요 시 서버에 업로드
            }
    }

    companion object {
        private const val TAG = "FCM"

        /** Manifest meta-data 와 반드시 동일해야 함 */
        const val FCM_CHANNEL_ID = "fcm_alert"
    }
}


