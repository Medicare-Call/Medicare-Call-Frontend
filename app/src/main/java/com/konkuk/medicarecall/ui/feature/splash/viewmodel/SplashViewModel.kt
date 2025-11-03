package com.konkuk.medicarecall.ui.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.domain.usecase.CheckLoginStatusUseCase
import com.konkuk.medicarecall.ui.model.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : ViewModel() {
    private val _navigationDestination = MutableStateFlow<NavigationDestination?>(null)
    val navigationDestination = _navigationDestination.asStateFlow()

    init {
        checkStatus()
    }

    private fun checkStatus() {
        viewModelScope.launch {
            val destination = checkLoginStatusUseCase()
            _navigationDestination.value = destination
        }
    }
}
