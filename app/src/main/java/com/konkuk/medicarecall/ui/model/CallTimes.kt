package com.konkuk.medicarecall.ui.model

// 어르신별 세 번의 콜 타임을 저장할 데이터 클래스
data class CallTimes(
    val first: Triple<Int, Int, Int>? = null,
    val second: Triple<Int, Int, Int>? = null,
    val third: Triple<Int, Int, Int>? = null,
)
