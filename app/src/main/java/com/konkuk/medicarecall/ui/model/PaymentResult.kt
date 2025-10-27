package com.konkuk.medicarecall.ui.model

data class PaymentResult(
    val success: Boolean,
    val orderCode: String?,
    val paymentId: String?,
    val resultCode: String?,
    val message: String?,
)
