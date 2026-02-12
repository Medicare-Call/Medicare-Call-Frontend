package com.konkuk.medicarecall.ui.model

data class ElderSubscription(
    val elderId: Long,
    val name: String,
    val plan: String,
    val price: Int,
    val nextBillingDate: String,
    val startDate: String,
)
