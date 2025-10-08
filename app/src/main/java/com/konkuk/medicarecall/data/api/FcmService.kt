package com.konkuk.medicarecall.data.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appPreferences.saveFcmToken(token)
                Log.d("FCM", "New FCM token saved successfully: $token")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to save FCM token", e)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message received: ${remoteMessage.data}")
        showNotification(remoteMessage)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val channelId = App.FCM_CHANNEL_ID
        val channelName = "FCM Notifications"

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(channelId)
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "FCM push notifications"
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPi = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_medi_app)
            .setContentTitle(remoteMessage.notification?.title ?: "새 알림")
            .setContentText(remoteMessage.notification?.body ?: "메시지가 도착했습니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(contentPi)
            .setAutoCancel(true)
            .setFullScreenIntent(contentPi, true)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val notificationId = (0..Int.MAX_VALUE).random()
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        } else {
            Log.w("FCM", "POST_NOTIFICATIONS permission not granted")
        }
    }
}
