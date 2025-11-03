package com.konkuk.medicarecall.data.api.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.konkuk.medicarecall.App
import com.konkuk.medicarecall.MainActivity
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.repository.FcmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // FCM 토큰을 DataStore에 비동기적으로 저장
        CoroutineScope(Dispatchers.IO).launch {
            fcmRepository.saveFcmToken(token)
            Log.d("FCM", "New FCM Token saved: $token")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message received: ${remoteMessage.data}")
        showNotification(remoteMessage)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val channelId = App.FCM_CHANNEL_ID
        val channelName = "FCM Notifications"

        // 채널 삭제 후 재생성 (IMPORTANCE_HIGH + PUBLIC)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(channelId)
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "FCM push notifications"
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        // 알림 클릭 시 MainActivity 실행
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPi = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        // 알림 빌더
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_medi_app) // 반드시 존재하는 리소스여야 함
            .setContentTitle(remoteMessage.notification?.title ?: "새 알림")
            .setContentText(remoteMessage.notification?.body ?: "메시지가 도착했습니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(contentPi)
            .setAutoCancel(true)
            .setFullScreenIntent(contentPi, true) // 화면 꺼져있을 때 팝업

        // 알림 권한 체크 후 notify
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationId = (0..Int.MAX_VALUE).random()
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        } else {
            Log.w("FCM", "POST_NOTIFICATIONS permission not granted")
        }
    }
}
