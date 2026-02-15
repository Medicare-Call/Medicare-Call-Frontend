package com.konkuk.medicarecall.data.api.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.konkuk.medicarecall.App
import com.konkuk.medicarecall.MainActivity
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.FcmRepository
import com.konkuk.medicarecall.data.repositoryimpl.FcmRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 다른 파일에서 직접 호출 안해도 되는 함수들 => FCM이나 안드로이드 시스템이 필요시 호출
 */
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmRepository: FcmRepository

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * FCM에서 해당 기기의 토큰 바꼈다고 알려줄 때 호출되는 콜백(
     * 앱 처음 설치, 앱 데이터 삭제, FCM 내부 정책 변경 등)
     * FCM쪽에서 알아서 불러주는 콜백(개발자가 직접 호출 X, 호출됐을 때 어떻게 저장할 지에 대한 코드)
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 1. 로컬에 저장
        serviceScope.launch {
            try {
                // FcmRepositoryImpl이 DataStore + 암호화 Serializer 사용
                // 평문 token만 넘겨줌
                fcmRepository.saveFcmToken(token)

                // 서버 갱신 (jwtToken은 DataStore에서 가져오거나, 로그인 시 저장된 값을 사용)
                val jwtToken = dataStoreRepository.getAccessToken() ?: return@launch
                (fcmRepository as? FcmRepositoryImpl)?.validateAndRefreshTokenIfNeeded(jwtToken)
            } catch (e: Exception) {
                // 토큰 저장 실패 무시
            }
        }
    }

    // 실제로 푸시가 도착했을 때 호출되는 콜백
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(remoteMessage)
    }

    // 알림 생성 및 표시 - 여기선 매번 채널 확인하고 없으면 만드는식으로 구성
    private fun showNotification(remoteMessage: RemoteMessage) {
        val channelId = App.FCM_CHANNEL_ID
        val channelName = "FCM Notifications"

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 채널 없으면 생성(보통은 한 번만 생성하고 재사용하는 쪽이 자연스러움)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(channelId) == null) {
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

        // 알림 내용 결정 (notification payload(서버에서 보낸 거)가 있으면 그걸 우선)
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "새 알림"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "메시지가 도착했습니다."

        // 알림 빌더
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_medi_app) // 반드시 존재하는 리소스여야 함
            .setContentTitle(title)
            .setContentText(body)
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
        }
    }
}
