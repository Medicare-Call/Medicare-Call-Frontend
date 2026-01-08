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
            savedStateHandle = savedStateHandle,
        )
    }
}
