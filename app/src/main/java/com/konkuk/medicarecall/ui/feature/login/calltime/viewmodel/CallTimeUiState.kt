package com.konkuk.medicarecall.ui.feature.login.calltime.viewmodel

import com.konkuk.medicarecall.ui.model.CallTimes

data class CallTimeUiState(
    val timeMap: Map<Long, CallTimes> = emptyMap(),
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val elderIds: List<Long> = emptyList(),
    val elderMap: Map<Long, String> = emptyMap(),
    val showBottomSheet: Boolean = false,
    val selectedIndex: Int = 0,
    val selectedTabIndex: Int = 0,
)
