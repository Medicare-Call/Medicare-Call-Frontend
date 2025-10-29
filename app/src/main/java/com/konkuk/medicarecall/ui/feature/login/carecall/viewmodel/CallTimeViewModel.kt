package com.konkuk.medicarecall.ui.feature.login.carecall.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.ui.model.CallTimes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallTimeViewModel @Inject constructor(
    private val setCallRepo: SetCallRepository,
) : ViewModel() {
    val timeMap = mutableStateMapOf<Int, CallTimes>()
    val isLoading = mutableStateOf(false)
    val lastError = mutableStateOf<Throwable?>(null)

    fun setTimes(id: Int, times: CallTimes) {
        timeMap[id] = times
    }

    fun isCompleteFor(id: Int): Boolean {
        val t = timeMap[id] ?: return false
        return t.first != null && t.second != null && t.third != null
    }

    fun isAllComplete(ids: List<Int>): Boolean =
        ids.isNotEmpty() && ids.all { isCompleteFor(it) }

    fun submitAllByIds(
        elderIds: List<Int>,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            isLoading.value = true
            lastError.value = null
            try {
                require(elderIds.isNotEmpty()) { "어르신 목록이 비어 있습니다." }
                val jobs = elderIds.map { id ->
                    val times = timeMap[id] ?: error("'$id'의 시간이 비어있습니다.")
                    async {
                        setCallRepo.saveForElder(id, times).getOrThrow()
                        Log.d("CallTimeViewModel", "Saved call times for id:$id")
                    }
                }
                jobs.awaitAll()
                onSuccess()
            } catch (t: Throwable) {
                Log.e("CallTimeViewModel", "submitAllByName failed", t)
                lastError.value = t
                onError(t)
            } finally {
                isLoading.value = false
            }
        }
    }
}
