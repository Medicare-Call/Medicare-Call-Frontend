package com.konkuk.medicarecall.ui.login_info.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.model.LoginState
import com.konkuk.medicarecall.data.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    val isLoggedIn = true // 추후 서버나 로컬에서 정보 받아오기

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Start)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
        }
        if (isLoggedIn) {
            _loginState.value = LoginState.LoggedIn
        } else {
            _loginState.value = LoginState.NotLoggedIn
        }

    }
}