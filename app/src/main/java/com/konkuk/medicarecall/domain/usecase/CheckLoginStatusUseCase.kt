package com.konkuk.medicarecall.domain.usecase

import android.util.Log
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.ui.model.NavigationDestination
import retrofit2.HttpException
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val eldersInfoRepository: EldersInfoRepository,
    private val elderIdRepository: ElderIdRepository,
) {
    /**
     * 앱의 초기 상태를 확인하여 다음에 이동할 화면을 결정합니다.
     * 이 과정에서 어떤 종류의 에러가 발생하더라도 로그인 화면으로 안내합니다.
     */
    suspend operator fun invoke(): NavigationDestination {
        return runCatching {
            // 1. 어르신 정보 확인
            val elders = eldersInfoRepository.getElders().getOrThrow()

            if (elders.isEmpty()) {
                // 어르신 정보가 없으면 등록 화면으로
                Log.d("httplog", "어르신 없음, 어르신 등록 화면으로")
                return@runCatching NavigationDestination.GoToRegisterElder
            }

            // 어르신 정보가 있으면 ID를 로컬에 저장
            elderIdRepository.clearElderId()
            elders.forEach { elderInfo ->
                elderIdRepository.addElderId(elderInfo.name, elderInfo.elderId)
            }
            Log.d("httplog", "어르신 정보 확인 완료, ID 저장됨")

            // 2. 시간 설정 확인 (로직 추가 예정)
            val elderIds = elderIdRepository.getElderIds()
            elderIds.forEach {
                eldersInfoRepository.getCareCallTimes(it.values.first())
                    .onSuccess { Log.d("httplog", "시간 설정 정보 확인 완료, $it") }
                    .onFailure { exception ->
                        when (exception) {
                            is HttpException -> {
                                val code = exception.code()
                                val errorBody = exception.response()?.errorBody()?.string()

                                Log.e("httplog", "HTTP 에러 발생 - 코드: $code, 메시지: $errorBody")

                                when (code) {
                                    404 -> {
                                        // 404 Not Found 에러 처리
                                        Log.d("httplog", "시간 설정 정보 없음, 시간 등록 화면으로")
                                        return@runCatching NavigationDestination.GoToTimeSetting
                                    }
                                    else -> {
                                        return@runCatching NavigationDestination.GoToLogin
                                    }
                                }
                            }
                        }
                    }
            }

            // 3. 결제(구독) 정보 확인
            val subscriptions = eldersInfoRepository.getSubscriptions().getOrThrow()

            if (subscriptions.isEmpty()) {
                // 구독 정보가 없으면 결제 화면으로
                Log.d("httplog", "구독 정보 없음, 결제 화면으로")
                return@runCatching NavigationDestination.GoToPayment
            }

            // 모든 정보가 있으면 홈 화면으로
            Log.d("httplog", "모든 정보 있음, 홈 화면으로")
            NavigationDestination.GoToHome
        }.getOrElse { exception ->
            // runCatching 블록 내에서 Exception이 발생하면 이 부분이 실행됩니다.
            Log.d("httplog", "상태 확인 중 Exception 발생. 로그인 화면으로 이동.", exception)
            Log.d("httplog", "Exception 메시지: ${exception.message}")
            NavigationDestination.GoToLogin
        }
    }
}
