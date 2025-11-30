package com.konkuk.medicarecall.ui.feature.login.payment.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {

    // 결제 대상 어르신 목록 (로컬 DataStore 기준)
    private val _elderMap = mutableStateOf<Map<Int, String>>(emptyMap())
    val elderMap get() = _elderMap.value

    // 결제 버튼 선택 여부
    val isPaymentSelected = mutableStateOf(false)

    // 월 요금 (상수)
    private val pricePerElder = 29_000

    init {
        observeElders()
    }

    // 어르신 목록 Flow → State
    private fun observeElders() {
        viewModelScope.launch {
            elderIdRepository.getElderIds()
                .collect { result ->
                    _elderMap.value = result
                }
        }
    }

    // 총 결제 금액
    fun getTotalPrice(): Int {
        return elderMap.size * pricePerElder
    }

    // 네이버페이 선택 토글
    fun togglePaymentSelect() {
        isPaymentSelected.value = !isPaymentSelected.value
    }
}
