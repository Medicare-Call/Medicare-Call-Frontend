package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship
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

    fun loadElderDataById(elderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    val elderData = list.firstOrNull { it.elderId == elderId }
                    _uiState.update { it.copy(elderData = elderData) }
                }
                .onFailure { exception ->
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateElderInfo(
        elderInfo: ElderInfo,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            updateElderInfoRepository.updateElderInfo(elderInfo)
                .onSuccess {
                    _uiState.update { it.copy(isSuccess = true) }
                    loadElderDataById(elderInfo.elderId)
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isSuccess = false) }
                }
        }
    }

    fun processElderInfo(
        elderId: Long,
        name: String,
        birthDate: String,
        gender: GenderType,
        phone: String,
        relationship: Relationship,
        residenceType: ElderResidence,
    ) {
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
                        _uiState.update { it.copy(isSuccess = true, isUpdateSuccess = true) }
                        if (elderId != -1L) {
                            loadElderDataById(elderId)
                        }
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        _uiState.update {
                            it.copy(isSuccess = false, errorMessage = "정보 수정을 실패했습니다. 다시 시도해주세요.")
                        }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteElderInfo(elderId: Long, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isDeleteSuccess = false) }
            try {
                eldersInfoRepository.deleteElder(elderId)
                    .onSuccess {
                        _uiState.update { it.copy(isDeleteSuccess = true) }
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
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

    fun updateRelationship(value: Relationship) {
        _uiState.update { it.copy(relationship = value) }
    }

    fun updateResidenceType(value: ElderResidence) {
        _uiState.update { it.copy(residenceType = value) }
    }

    fun setShowDeleteDialog(value: Boolean) {
        _uiState.update { it.copy(showDeleteDialog = value) }
    }
}
