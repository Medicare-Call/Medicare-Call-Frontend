package com.konkuk.medicarecall.data.di


import androidx.lifecycle.SavedStateHandle
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        HomeViewModel(
            eldersInfoRepository = get(),
            homeRepository = get(),
            eldersHealthInfoRepository = get(),
            savedStateHandle = savedStateHandle
            // 화면 재생성 및 프로세스 복구를 위해 elderId를 SavedStateHandle로 관리
            // SavedStateHandle은 Android 전용으로 KMP 공용 대상이 아님
        )
    }
}

