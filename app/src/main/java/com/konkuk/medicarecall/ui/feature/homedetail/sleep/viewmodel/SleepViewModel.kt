package com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.SleepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val sleepRepository: SleepRepository,
) : ViewModel() {

    private companion object {
        const val TAG = "SLEEP_API"
    }

    private val _sleepState = MutableStateFlow(SleepUiState.Companion.EMPTY)
    val sleep: StateFlow<SleepUiState> = _sleepState

    fun loadSleepDataForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            try {
                _sleepState.value = sleepRepository.getSleepUiState(
                    elderId = elderId,
                    date = date,
                )
                Log.i(TAG, "Success elderId=$elderId, date=$formatted")
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> {
                                // 미기록
                                Log.i(TAG, "No data (404) elderId=$elderId, date=$formatted")
                                _sleepState.value = SleepUiState.Companion.EMPTY
                            }

                            400 -> {
                                Log.w(
                                    TAG,
                                    "Bad request (400) elderId=$elderId, date=$formatted, msg=${e.message()}",
                                )
                                _sleepState.value = SleepUiState.Companion.EMPTY
                            }

                            401, 403 -> {
                                Log.w(TAG, "Unauthorized (${e.code()}) elderId=$elderId")
                                _sleepState.value = SleepUiState.Companion.EMPTY
                            }

                            else -> {
                                Log.e(
                                    TAG,
                                    "API error code=${e.code()} elderId=$elderId, date=$formatted",
                                    e,
                                )
                                _sleepState.value = SleepUiState.Companion.EMPTY
                            }
                        }
                    }

                    else -> {
                        Log.e(TAG, "Unexpected error elderId=$elderId, date=$formatted", e)
                        _sleepState.value = SleepUiState.Companion.EMPTY
                    }
                }
            }
        }
    }
}
