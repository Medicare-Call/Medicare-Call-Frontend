package com.konkuk.medicarecall.ui.feature.login.payment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NaverPayViewModel(
    private val naverPayRepo: NaverPayRepository,
    private val elderInfoRepo: EldersInfoRepository,
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    // 1. 상태 관리 변수 (표준 규격)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _orderCode = MutableStateFlow<String?>(null)
    val orderCode: StateFlow<String?> = _orderCode.asStateFlow()

    private val _eldersIdList = MutableStateFlow<List<Int>>(emptyList())
    val eldersIdList: StateFlow<List<Int>> = _eldersIdList.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    init {
        postNaverPayInfo()
    }

    /** 네이버페이 결제 예약 정보 전송 */
    fun postNaverPayInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. 어르신 목록 먼저 가져오기 (결제 금액 계산을 위해 필수)
                val eldersResult = elderInfoRepo.getElders()

                eldersResult.onSuccess { elders ->
                    val ids = elders.map { it.elderId }
                    _eldersIdList.value = ids

                    // 2. 어르신 목록이 확보된 후 결제 정보 생성 및 전송
                    val productCount = ids.size
                    val unitPrice = 29000
                    val totalPrice = unitPrice * productCount

                    val payInfo = ReservePayRequestDto(
                        productName = "메디케어콜 프리미엄 플랜",
                        productCount = productCount,
                        totalPayAmount = totalPrice,
                        taxScopeAmount = totalPrice,
                        taxExScopeAmount = 0,
                        elderIds = ids,
                    )

                    _accessToken.value = dataStoreRepo.getAccessToken()

                    Log.d("NaverPayViewModel", "결제 예약 요청: $payInfo")

                    naverPayRepo.postReserveInfo(payInfo)
                        .onSuccess { resp ->
                            Log.d("NaverPayViewModel", "결제 예약 성공: ${resp.body.code}")
                            _orderCode.value = resp.body.code
                        }
                        .onFailure { e ->
                            if (e is CancellationException) throw e
                            Log.e("NaverPayViewModel", "결제 예약 실패", e)
                            _errorMessage.value = "결제 준비 중 오류가 발생했습니다."
                        }
                }.onFailure { e ->
                    if (e is CancellationException) throw e
                    Log.e("NaverPayViewModel", "어르신 정보 로딩 실패", e)
                    _errorMessage.value = "어르신 정보를 불러오지 못했습니다."
                }

            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                Log.e("NaverPayViewModel", "예상치 못한 오류", e)
                _errorMessage.value = "네트워크 연결을 확인해주세요."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 상태 초기화 */
    fun resetStatus() {
        _errorMessage.value = null
    }
}
