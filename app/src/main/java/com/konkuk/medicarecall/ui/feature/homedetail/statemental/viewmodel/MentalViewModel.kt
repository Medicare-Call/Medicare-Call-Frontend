package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MentalViewModel @Inject constructor(
    private val mentalRepository: MentalRepository,
) : ViewModel() {

    private companion object {
        const val TAG = "MENTAL_API"
    }

    private val _mental = MutableStateFlow(MentalUiState.Companion.EMPTY)
    val mental: StateFlow<MentalUiState> = _mental

    fun loadMental(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            val ui = mentalRepository.getMentalUiState(elderId, date)
            Log.d("MENTAL_VM", "ui=$ui")
            _mental.value = ui
        }
    }

    fun loadMentalDataForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            try {
                val ui = mentalRepository.getMentalUiState(elderId, date)
                _mental.value = ui

                if (ui.isRecorded) {
                    Log.i(TAG, "Loaded data: $ui")
                } else {
                    Log.i(TAG, "No data (EMPTY) elderId=$elderId, date=$formatted")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.i(TAG, "No data (404) elderId=$elderId, date=$formatted")
                    _mental.value = MentalUiState.Companion.EMPTY
                } else {
                    Log.e(TAG, "API error code=${e.code()} elderId=$elderId, date=$formatted", e)
                    _mental.value = MentalUiState.Companion.EMPTY
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error elderId=$elderId, date=$formatted", e)
                _mental.value = MentalUiState.Companion.EMPTY
            }
        }
    }
}
