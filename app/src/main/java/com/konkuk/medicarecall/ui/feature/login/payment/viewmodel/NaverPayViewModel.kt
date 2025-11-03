package com.konkuk.medicarecall.ui.feature.login.payment.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaverPayViewModel @Inject constructor(
    private val naverPayRepo: NaverPayRepository,
    private val elderInfoRepo: EldersInfoRepository,
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    var eldersIdList by mutableStateOf<List<Int>>(emptyList())
        private set

    init {
        postNaverPayInfo()
    }

    var orderCode by mutableStateOf<String?>(null)
        private set

    var accessToken by mutableStateOf<String?>(null)
        private set

    fun postNaverPayInfo() {
        viewModelScope.launch {
            elderInfoRepo.getElders()
                .onSuccess {
                    Log.d("EldersInfoViewModel", "노인 개인 정보 불러오기 성공: ${it.size}개")
                    eldersIdList = it.map { elder -> elder.elderId }
                    Log.d("EldersInfoViewModel", "노인 아이디 정보: $eldersIdList")
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("EldersInfoViewModel", "노인 개인 정보 로딩 실패: ${it.message}", it)
                }

            val payInfo = ReservePayRequestDto(
                productName = "메디케어콜 프리미엄 플랜",
                productCount = eldersIdList.size,
                totalPayAmount = 29000 * eldersIdList.size,
                taxScopeAmount = 29000 * eldersIdList.size, // 현재는 총금액과 동일하게
                taxExScopeAmount = 0,
                elderIds = eldersIdList,
            )
            Log.d("NaverPayViewModel", "네이버페이 토큰 값 불러오기")
            accessToken = dataStoreRepo.getAccessToken()

            Log.d("NaverPayViewModel", "네이버페이 결제 정보 전송 요청: $payInfo")
            naverPayRepo.postReserveInfo(payInfo)
                .onSuccess { resp ->
                    Log.d("NaverPayViewModel", "네이버페이 결제 전송 성공: $resp")
                    orderCode = resp.body.code
                    Log.d("NaverPayViewModel", "order code : $orderCode")
                }
                .onFailure { exception ->
                    Log.e("NaverPayViewModel", "네이버페이 결제 정보 전송 실패: ${exception.message}", exception)
                }
        }
    }
}
