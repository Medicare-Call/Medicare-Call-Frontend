package com.konkuk.medicarecall.ui.model

data class ElderData(
    val name: String = "",
    val birthDate: String = "",
    val gender: Boolean = true,
    val phoneNumber: String = "",
    val relationship: String = "",
    val livingType: String = "",
    var id: Int? = null,
)
