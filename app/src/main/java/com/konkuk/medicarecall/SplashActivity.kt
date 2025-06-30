package com.konkuk.medicarecall

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.PersistableBundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat.startActivity
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import kotlinx.coroutines.delay
import kotlin.jvm.java


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MediCareCallTheme {
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {

        delay(1500L)

        // Main Activity로 이동
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }

    Box(
        modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.main),
    ) {
        Image(
            painterResource(R.drawable.bg_splash),
            "Medicare Call 스플래시",
            modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds //
        )
    }
}