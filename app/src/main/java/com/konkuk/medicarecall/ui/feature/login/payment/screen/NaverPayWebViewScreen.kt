package com.konkuk.medicarecall.ui.feature.login.payment.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.feature.login.payment.viewmodel.NaverPayViewModel
import com.konkuk.medicarecall.ui.feature.settings.component.SettingsTopAppBar
import com.konkuk.medicarecall.ui.model.PaymentResult
import com.konkuk.medicarecall.ui.theme.MediCareCallTheme
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NaverPayWebViewScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToFinish: () -> Unit = {},
    naverPayViewModel: NaverPayViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MediCareCallTheme.colors.bg)
            .systemBarsPadding()
            .imePadding(),
    ) {
        SettingsTopAppBar(
            modifier = modifier,
            title = "결제하기",
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_back),
                    contentDescription = "go_back",
                    modifier = modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color.Black,
                )
            },
            leftIconClick = onBack,
            rightIcon = {
                // 임시 이동 버튼 (실배포 전 제거)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_big),
                    contentDescription = "go_next",
                    modifier = modifier.size(24.dp),
                    tint = Color.Black,
                )
            },
            rightIconClick = navigateToFinish,
        )
        val context = LocalContext.current
        val baseHost = "medicare-call.shop"
        var popupWebView by remember { mutableStateOf<WebView?>(null) }
        // ViewModel 상태 스냅샷
        val orderCode = naverPayViewModel.orderCode
        val accessToken = naverPayViewModel.accessToken
        // 중복 네비 방지
        var navigated by rememberSaveable { mutableStateOf(false) }
        // WebView 인스턴스 재사용
        val webView = remember { WebView(context) }
        var loaded by remember { mutableStateOf(false) }
        LaunchedEffect(orderCode, accessToken) {
            if (!loaded && !orderCode.isNullOrBlank() && !accessToken.isNullOrBlank()) {
                val url = "https://$baseHost/api/payments/page/$orderCode"
                val authHeaders = mapOf("Authorization" to "Bearer $accessToken")
                Log.d("NaverPayScreen", "Loading Naver Pay URL: $url with headers: $authHeaders")
                webView.loadUrl(url, authHeaders)
                loaded = true
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MediCareCallTheme.colors.gray2),
            contentAlignment = Alignment.Center,
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    webView.apply {
                        // 1) 쿠키 허용 + 3rd-party 쿠키 허용 (네이버 도메인 세션 유지)
                        val cm = CookieManager.getInstance()
                        cm.setAcceptCookie(true)
                        cm.setAcceptThirdPartyCookies(this, true)
                        // 2) JS/스토리지 + 팝업 허용
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.setSupportMultipleWindows(true)
                        // 3) 새창 요청을 같은 WebView에서 처리
                        webChromeClient = object : WebChromeClient() {
                            override fun onCreateWindow(
                                view: WebView?,
                                isDialog: Boolean,
                                isUserGesture: Boolean,
                                resultMsg: Message?,
                            ): Boolean {
                                val ctx = view?.context ?: return false
                                // 새 팝업 WebView 생성
                                val popup = WebView(ctx).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.setSupportMultipleWindows(true)
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    // 쿠키(3rd party 포함)
                                    val cm = CookieManager.getInstance()
                                    cm.setAcceptCookie(true)
                                    cm.setAcceptThirdPartyCookies(this, true)
                                    // ─────────────────────────────────────────────
                                    // ① 팝업에도 동일한 JS 브릿지(이름 "Android") 추가
                                    //    payment-result.html에서 Android.onPaymentComplete/closePage() 호출
                                    // ─────────────────────────────────────────────
                                    addJavascriptInterface(
                                        object {
                                            @JavascriptInterface
                                            fun onPaymentComplete(json: String) {
                                                Handler(Looper.getMainLooper())
                                                    .post {
                                                        try {
                                                            val o = JSONObject(json)
                                                            val result =
                                                                PaymentResult(
                                                                    success = o.optBoolean(
                                                                        "success",
                                                                        false,
                                                                    ),
                                                                    orderCode = o.optString(
                                                                        "orderCode",
                                                                        null,
                                                                    ),
                                                                    paymentId = o.optString(
                                                                        "paymentId",
                                                                        null,
                                                                    ),
                                                                    resultCode = o.optString(
                                                                        "resultCode",
                                                                        null,
                                                                    ),
                                                                    message = o.optString(
                                                                        "message",
                                                                        null,
                                                                    ),
                                                                )
                                                            Log.d(
                                                                "NaverPayScreen",
                                                                "Popup payment result: $result",
                                                            )
                                                            if (result.success && !navigated) {
                                                                navigated = true
                                                                // 팝업 닫고 성공 네비게이션
                                                                popupWebView?.destroy()
                                                                popupWebView = null
                                                                navigateToFinish()
                                                            } else if (!result.success) {
                                                                Log.w(
                                                                    "NaverPayScreen",
                                                                    "Payment failed: ${result.message ?: "unknown"}",
                                                                )
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e(
                                                                "NaverPayScreen",
                                                                "Parse error: ${e.message}",
                                                            )
                                                        }
                                                    }
                                            }

                                            @JavascriptInterface
                                            fun closePage() {
                                                Handler(Looper.getMainLooper())
                                                    .post {
                                                        popupWebView?.destroy()
                                                        popupWebView = null
                                                    }
                                            }
                                        },
                                        "Android",
                                    )
                                    // ─────────────────────────────────────────────
                                    // ② 팝업 내 네비게이션/스킴 처리 + fallback JS
                                    // ─────────────────────────────────────────────
                                    webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(
                                            v: WebView,
                                            req: WebResourceRequest,
                                        ): Boolean {
                                            val url = req.url.toString()
                                            val host = req.url.host ?: ""
                                            val scheme = req.url.scheme ?: ""
                                            // intent://, market:// 등 외부 스킴
                                            if (scheme.equals("intent", true)) {
                                                try {
                                                    val intent = Intent.parseUri(
                                                        url,
                                                        Intent.URI_INTENT_SCHEME,
                                                    )
                                                    try {
                                                        v.context.startActivity(intent)
                                                    } catch (_: Exception) {
                                                        val fallback =
                                                            intent.getStringExtra("browser_fallback_url")
                                                        if (!fallback.isNullOrEmpty()) {
                                                            v.loadUrl(fallback)
                                                        } else {
                                                            val pkg = intent.`package`
                                                            if (!pkg.isNullOrEmpty()) {
                                                                val market = Intent(
                                                                    Intent.ACTION_VIEW,
                                                                    ("market://details?id=" + pkg).toUri(),
                                                                )
                                                                v.context.startActivity(market)
                                                            }
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e(
                                                        "NaverPayWebView",
                                                        "intent scheme error: ${e.message}",
                                                    )
                                                }
                                                return true
                                            }
                                            if (scheme in listOf("tel", "mailto", "sms")) {
                                                try {
                                                    val i = Intent(
                                                        Intent.ACTION_VIEW,
                                                        req.url,
                                                    )
                                                    v.context.startActivity(i)
                                                } catch (_: Exception) {
                                                }
                                                return true
                                            }
                                            // 우리 서버 이동 시 토큰 재부착 (팝업에서도 동일 정책)
                                            if (host == "medicare-call.shop") {
                                                val token = naverPayViewModel.accessToken
                                                if (!token.isNullOrBlank()) {
                                                    v.loadUrl(
                                                        url,
                                                        mapOf("Authorization" to "Bearer $token"),
                                                    )
                                                    return true
                                                }
                                            }
                                            return false // 나머지는 WebView가 처리
                                        }

                                        override fun onPageFinished(v: WebView, url: String) {
                                            Log.d(
                                                "NaverPayWebView",
                                                "POPUP FINISHED: $url",
                                            )
                                            // 결제 결과 페이지 방어적 fallback 호출
                                            if (url.contains("/api/payments/result")) {
                                                v.evaluateJavascript(
                                                    """
                                                    (function() {
                                                      try {
                                                        if (window.Android && Android.onPaymentComplete && window.paymentResult) {
                                                          Android.onPaymentComplete(JSON.stringify(window.paymentResult));
                                                          return 'bridged';
                                                        }
                                                        return 'no-bridge-or-result';
                                                      } catch (e) {
                                                        return 'error:' + (e && e.message);
                                                      }
                                                    })();
                                                    """.trimIndent(),
                                                ) { ret ->
                                                    Log.d(
                                                        "NaverPayWebView",
                                                        "popup fallback: $ret",
                                                    )
                                                }
                                            }
                                        }

                                        override fun onReceivedHttpError(
                                            v: WebView,
                                            req: WebResourceRequest,
                                            res: WebResourceResponse,
                                        ) {
                                            Log.e(
                                                "NaverPayWebView",
                                                "POPUP HTTP ${res.statusCode} on ${req.url}",
                                            )
                                        }

                                        override fun onReceivedError(
                                            v: WebView,
                                            req: WebResourceRequest,
                                            err: WebResourceError,
                                        ) {
                                            Log.e(
                                                "NaverPayWebView",
                                                "POPUP ERR ${err.errorCode} on ${req.url}: ${err.description}",
                                            )
                                        }
                                    }
                                    // 팝업 자체의 close 제어
                                    webChromeClient = object : WebChromeClient() {
                                        override fun onCloseWindow(window: WebView?) {
                                            popupWebView?.destroy()
                                            popupWebView = null
                                        }
                                    }
                                }
                                // Compose 쪽에 팝업 표시
                                popupWebView = popup
                                // 새 창에 popup WebView를 연결
                                val transport =
                                    resultMsg?.obj as? WebView.WebViewTransport ?: return false
                                transport.webView = popup
                                resultMsg.sendToTarget()
                                return true
                            }

                            override fun onConsoleMessage(msg: ConsoleMessage?): Boolean {
                                Log.d("NaverPayWebView", "CONSOLE: ${msg?.message()}")
                                return super.onConsoleMessage(msg)
                            }
                        }
                        // 4) 네비게이션 처리: 우리 도메인은 Authorization 재부착, intent 스킴 처리
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest,
                            ): Boolean {
                                val url = request.url.toString()
                                val host = request.url.host ?: ""
                                val scheme = request.url.scheme ?: ""
                                // intent://, market:// 등 외부 스킴 처리
                                if (scheme.equals("intent", true)) {
                                    try {
                                        val intent = Intent.parseUri(
                                            url,
                                            Intent.URI_INTENT_SCHEME,
                                        )
                                        try {
                                            view.context.startActivity(intent)
                                        } catch (_: Exception) {
                                            val fallback =
                                                intent.getStringExtra("browser_fallback_url")
                                            if (!fallback.isNullOrEmpty()) {
                                                view.loadUrl(fallback)
                                            } else {
                                                val pkg = intent.`package`
                                                if (!pkg.isNullOrEmpty()) {
                                                    val market = Intent(
                                                        Intent.ACTION_VIEW,
                                                        "market://details?id=$pkg".toUri(),
                                                    )
                                                    view.context.startActivity(market)
                                                }
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e(
                                            "NaverPayWebView",
                                            "intent scheme error: ${e.message}",
                                        )
                                    }
                                    return true
                                }
                                if (scheme in listOf("tel", "mailto", "sms")) {
                                    try {
                                        val i = Intent(
                                            Intent.ACTION_VIEW,
                                            request.url,
                                        )
                                        view.context.startActivity(i)
                                    } catch (_: Exception) {
                                    }
                                    return true
                                }
                                // 우리 서버 이동일 땐 토큰 다시 붙여서 로드
                                if (host == "medicare-call.shop") {
                                    val token = naverPayViewModel.accessToken
                                    if (!token.isNullOrBlank()) {
                                        view.loadUrl(url, mapOf("Authorization" to "Bearer $token"))
                                        return true
                                    }
                                }
                                // 그 외(네이버 포함)는 WebView가 자체 처리
                                return false
                            }

                            override fun onPageFinished(view: WebView, url: String) {
                                Log.d("NaverPayWebView", "FINISHED: $url")
                                // 우리 서버의 결제 완료 페이지에 도달했을 때만 시도
                                if (url.contains("/api/payments/result")) {
                                    view.evaluateJavascript(
                                        """
                                        (function() {
                                          try {
                                            if (window.Android && Android.onPaymentComplete && window.paymentResult) {
                                              Android.onPaymentComplete(JSON.stringify(window.paymentResult));
                                              return 'bridged';
                                            }
                                            return 'no-bridge-or-result';
                                          } catch (e) {
                                            return 'error:' + (e && e.message);
                                          }
                                        })();
                                        """.trimIndent(),
                                    ) { ret -> Log.d("NaverPayWebView", "fallback bridge: $ret") }
                                }
                            } // 서버가 호출안하면 우리가 호출 (결과 페이지 도달)

                            override fun onReceivedHttpError(
                                view: WebView,
                                request: WebResourceRequest,
                                errorResponse: WebResourceResponse,
                            ) {
                                Log.e(
                                    "NaverPayWebView",
                                    "HTTP ${errorResponse.statusCode} on ${request.url}",
                                )
                            }

                            override fun onReceivedError(
                                view: WebView,
                                request: WebResourceRequest,
                                error: WebResourceError,
                            ) {
                                Log.e(
                                    "NaverPayWebView",
                                    "ERR ${error.errorCode} on ${request.url}: ${error.description}",
                                )
                            }
                        }
                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun onPaymentComplete(json: String) {
                                    Log.d("NaverPayScreen", "Payment complete callback: $json")
                                    Handler(Looper.getMainLooper()).post {
                                        try {
                                            val o = JSONObject(json)
                                            val result = PaymentResult(
                                                success = o.optBoolean("success", false),
                                                orderCode = o.optString("orderCode", null),
                                                paymentId = o.optString("paymentId", null),
                                                resultCode = o.optString("resultCode", null),
                                                message = o.optString("message", null),
                                            )
                                            Log.d("NaverPayScreen", "Payment result: $result")
                                            if (result.success && !navigated) {
                                                navigated = true
                                                Log.d("NaverPayScreen", "navigate FinishSplash")
                                                navigateToFinish()
                                            } else if (!result.success) {
                                                Log.w(
                                                    "NaverPayScreen",
                                                    "Payment failed: ${result.message ?: "unknown"}",
                                                )
                                                // TODO: 실패 UI/토스트 등
                                                onBack()
                                            }
                                        } catch (e: Exception) {
                                            Log.e("NaverPayScreen", "Parse error: ${e.message}")
                                        }
                                    }
                                }
                            },
                            "Android",
                        )
                    }
                },
            )
            if (popupWebView != null) {
                Dialog(
                    onDismissRequest = {
                        popupWebView?.destroy()
                        popupWebView = null
                    },
                ) {
                    AndroidView(
                        factory = { popupWebView!! },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            if (orderCode.isNullOrBlank() || accessToken.isNullOrBlank()) {
                Log.d("NaverPayScreen", "Waiting for orderCode/accessToken...")
                CircularProgressIndicator(
                    color = MediCareCallTheme.colors.main,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}
