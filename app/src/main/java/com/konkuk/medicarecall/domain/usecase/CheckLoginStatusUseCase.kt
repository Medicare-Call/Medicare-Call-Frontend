package com.konkuk.medicarecall.domain.usecase

import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.ui.model.NavigationDestination
import org.koin.core.annotation.Factory
import com.konkuk.medicarecall.data.exception.HttpException

@Factory
class CheckLoginStatusUseCase(
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
                return@runCatching NavigationDestination.GoToRegisterElder
            }

            // 어르신 정보가 있으면 ID를 로컬에 저장
            val elderIdMap: MutableMap<Long, String> = mutableMapOf()
            elders.forEach { elderInfo ->
                elderIdMap.put(elderInfo.elderId, elderInfo.name)
            }
            elderIdRepository.updateElderIds(elderIdMap)

            // 2. 시간 설정 확인
            elderIdMap.forEach {
                eldersInfoRepository.getCareCallTimes(it.key)
                    .onSuccess { }
                    .onFailure { exception ->
                        when (exception) {
                            is HttpException -> {
                                val code = exception.code()
                                val errorBody = exception.response()?.errorBody()?.toString()

                                when (code) {
                                    404 -> {
                                        // 404 Not Found 에러 처리
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
                return@runCatching NavigationDestination.GoToPayment
            }

            // 모든 정보가 있으면 홈 화면으로
            NavigationDestination.GoToHome
        }.getOrElse { exception ->
            // runCatching 블록 내에서 Exception이 발생하면 이 부분이 실행됩니다.
            NavigationDestination.GoToLogin
        }
    }
}
