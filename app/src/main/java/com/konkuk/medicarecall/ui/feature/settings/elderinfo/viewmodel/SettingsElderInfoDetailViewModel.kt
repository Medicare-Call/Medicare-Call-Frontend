package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.ui.model.ElderInfo
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsElderInfoDetailViewModel(
    private val eldersInfoRepository: EldersInfoRepository,
    private val updateElderInfoRepository: UpdateElderInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsElderInfoDetailUiState())
    val uiState: StateFlow<SettingsElderInfoDetailUiState> = _uiState.asStateFlow()

    fun loadElderDataById(elderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    val elderData = list.firstOrNull { it.elderId == elderId }
                    _uiState.update { it.copy(elderData = elderData) }
                    if (elderData == null) {
                        Log.w("SettingsElderInfoDetailViewModel", "어르신 정보를 찾을 수 없습니다. elderId: $elderId")
                    }
                }
                .onFailure { exception ->
                    Log.e("SettingsElderInfoDetailViewModel", "어르신 정보 로딩 실패", exception)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateElderInfo(
        elderInfo: ElderInfo,
        onComplete: (() -> Unit)? = null,
    ) {
        Log.d("SettingsElderInfoDetailViewModel", "어르신 개인 정보 수정 요청: elderId=${elderInfo.elderId}")

        viewModelScope.launch {
            updateElderInfoRepository.updateElderInfo(elderInfo)
                .onSuccess {
                    Log.d("SettingsElderInfoDetailViewModel", "어르신 개인 정보 수정 완료: $it")
                    _uiState.update { it.copy(isSuccess = true) }
                    loadElderDataById(elderInfo.elderId)
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    Log.e("SettingsElderInfoDetailViewModel", "어르신 개인 정보 수정 실패: $exception")
                    _uiState.update { it.copy(isSuccess = false) }
                }
        }
    }

    fun processElderInfo(
        elderId: Int,
        name: String,
        birthDate: String,
        gender: GenderType,
        phone: String,
        relationship: RelationshipType,
        residenceType: ElderResidenceType,
    ) {
        Log.d("SettingsElderInfoDetailViewModel", "어르신 정보 처리 요청 (등록/수정): elderId=$elderId")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isUpdateSuccess = false, errorMessage = null) }
            try {
                val elderInfo = ElderInfo(
                    elderId = elderId,
                    name = name,
                    birthDate = birthDate,
                    gender = gender,
                    phone = phone,
                    relationship = relationship,
                    residenceType = residenceType,
                )
                updateElderInfoRepository.updateElderInfo(elderInfo)
                    .onSuccess {
                        Log.d("SettingsElderInfoDetailViewModel", "어르신 정보 처리 완료: $it")
                        _uiState.update { it.copy(isSuccess = true, isUpdateSuccess = true) }
                        if (elderId != -1) {
                            loadElderDataById(elderId)
                        }
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        Log.e("SettingsElderInfoDetailViewModel", "어르신 정보 처리 실패: $exception")
                        _uiState.update {
                            it.copy(isSuccess = false, errorMessage = "정보 수정을 실패했습니다. 다시 시도해주세요.")
                        }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteElderInfo(elderId: Int, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isDeleteSuccess = false) }
            try {
                eldersInfoRepository.deleteElder(elderId)
                    .onSuccess {
                        Log.d("SettingsElderInfoDetailViewModel", "어르신 정보 삭제 완료: $it")
                        _uiState.update { it.copy(isDeleteSuccess = true) }
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        Log.e("SettingsElderInfoDetailViewModel", "어르신 정보 삭제 실패: $exception")
                        _uiState.update { it.copy(errorMessage = "정보 삭제를 실패했습니다.") }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(isUpdateSuccess = false, isDeleteSuccess = false, errorMessage = null) }
    }

    fun initializeForm(elderInfo: ElderInfo) {
        _uiState.update {
            it.copy(
                isMale = elderInfo.gender == GenderType.MALE,
                name = elderInfo.name,
                phoneNum = elderInfo.phone,
                relationship = elderInfo.relationship,
                residenceType = elderInfo.residenceType,
                birth = elderInfo.birthDate.replace("-", ""),
            )
        }
    }

    fun updateIsMale(value: Boolean) {
        _uiState.update { it.copy(isMale = value) }
    }

    fun updateName(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun updateBirth(value: String) {
        _uiState.update { it.copy(birth = value) }
    }

    fun updatePhoneNum(value: String) {
        _uiState.update { it.copy(phoneNum = value) }
    }

    fun updateRelationship(value: RelationshipType) {
        _uiState.update { it.copy(relationship = value) }
    }

    fun updateResidenceType(value: ElderResidenceType) {
        _uiState.update { it.copy(residenceType = value) }
    }

    fun setShowDeleteDialog(value: Boolean) {
        _uiState.update { it.copy(showDeleteDialog = value) }
    }
}
