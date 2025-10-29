package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
) : ViewModel() {

    private companion object {
        const val TAG = "MED_API"
    }

    data class ScreenState(
        val loading: Boolean = false,
        val items: List<MedicineUiState> = emptyList(),
        val emptyDate: LocalDate? = null,
        val hasConfiguredMeds: Boolean = false,
    )

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    fun loadMedicinesForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            _state.update { it.copy(loading = true, emptyDate = null) }
            try {
                val daily = medicineRepository.getMedicineUiStateList(elderId, date)

                if (daily.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            loading = false,
                            items = daily,
                            emptyDate = null,
                            hasConfiguredMeds = true,
                        )
                    }
                    return@launch
                }

                val configured = medicineRepository.getConfiguredMedicineUiList(elderId)
                if (configured.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            loading = false,
                            items = configured,
                            emptyDate = null,
                            hasConfiguredMeds = true,
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            loading = false,
                            items = emptyList(),
                            emptyDate = date,
                            hasConfiguredMeds = false,
                        )
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404, 400, 401, 403 -> {
                                val tag = when (e.code()) {
                                    404 -> "No data (404)"
                                    400 -> "Bad request (400): ${e.message()}"
                                    401, 403 -> "Unauthorized (${e.code()})"
                                    else -> ""
                                }
                                Log.w(TAG, "$tag elderId=$elderId, date=$formatted")
                                _state.update {
                                    it.copy(
                                        loading = false,
                                        items = emptyList(),
                                        emptyDate = date,
                                    )
                                }
                            }

                            else -> {
                                Log.e(
                                    TAG,
                                    "API error code=${e.code()} elderId=$elderId, date=$formatted",
                                    e,
                                )
                                _state.update {
                                    it.copy(
                                        loading = false,
                                        items = emptyList(),
                                        emptyDate = date,
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        Log.e(TAG, "Unexpected error elderId=$elderId, date=$formatted", e)
                        _state.update {
                            it.copy(
                                loading = false,
                                items = emptyList(),
                                emptyDate = date,
                            )
                        }
                    }
                }
            }
        }
    }
}
