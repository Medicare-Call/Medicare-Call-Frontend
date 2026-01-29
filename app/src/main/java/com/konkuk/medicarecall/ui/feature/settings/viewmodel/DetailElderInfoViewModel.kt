package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailElderInfoViewModel(
    private val eldersInfoRepository: EldersInfoRepository,
    private val updateElderInfoRepository: UpdateElderInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EldersInfoResponseDto?>(null)
    val uiState: StateFlow<EldersInfoResponseDto?> = _uiState.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadElderDataById(elderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    val elderData = list.firstOrNull { it.elderId == elderId }
                    _uiState.value = elderData
                    if (elderData == null) {
                        Log.w("DetailElderInfoViewModel", "어르신 정보를 찾을 수 없습니다. elderId: $elderId")
                    }
                }
                .onFailure { exception ->
                    Log.e("DetailElderInfoViewModel", "어르신 정보 로딩 실패", exception)
                }
                .also {
                    _isLoading.value = false
                }
        }
    }

    private val _isMale = MutableStateFlow(false)
    val isMale: StateFlow<Boolean> = _isMale.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _birth = MutableStateFlow("")
    val birth: StateFlow<String> = _birth.asStateFlow()

    private val _phoneNum = MutableStateFlow("")
    val phoneNum: StateFlow<String> = _phoneNum.asStateFlow()

    private val _relationship = MutableStateFlow(RelationshipType.ACQUAINTANCE)
    val relationship: StateFlow<RelationshipType> = _relationship.asStateFlow()

    private val _residenceType = MutableStateFlow(ElderResidenceType.WITH_FAMILY)
    val residenceType: StateFlow<ElderResidenceType> = _residenceType.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    // Async State
    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess: StateFlow<Boolean> = _isDeleteSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun updateElderInfo(
        elderInfo: EldersInfoResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        val updateInfo = ElderRegisterRequestDto(
            name = elderInfo.name,
            birthDate = elderInfo.birthDate,
            gender = elderInfo.gender,
            phone = elderInfo.phone,
            relationship = elderInfo.relationship,
            residenceType = elderInfo.residenceType,
        )
        Log.d("DetailElderInfoViewModel", "어르신 개인 정보 수정 요청: elderId=${elderInfo.elderId}")

        viewModelScope.launch {
            updateElderInfoRepository.updateElderInfo(
                id = elderInfo.elderId,
                request = updateInfo,
            )
                .onSuccess {
                    Log.d("DetailElderInfoViewModel", "어르신 개인 정보 수정 완료: $it")
                    _isSuccess.value = true
                    loadElderDataById(elderInfo.elderId)
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    Log.e("DetailElderInfoViewModel", "어르신 개인 정보 수정 실패: $exception")
                    _isSuccess.value = false
                }
        }
    }

    fun processElderInfo(elderId: Int, request: ElderRegisterRequestDto) {
        Log.d("DetailElderInfoViewModel", "어르신 정보 처리 요청 (등록/수정): elderId=$elderId")
        viewModelScope.launch {
            _isLoading.value = true
            _isUpdateSuccess.value = false
            _errorMessage.value = null
            try {
                updateElderInfoRepository.updateElderInfo(
                    id = elderId,
                    request = request,
                )
                    .onSuccess {
                        Log.d("DetailElderInfoViewModel", "어르신 정보 처리 완료: $it")
                        _isSuccess.value = true
                        _isUpdateSuccess.value = true
                        // 수정 모드(elderId != -1)일 때만 데이터 재로드
                        if (elderId != -1) {
                            loadElderDataById(elderId)
                        }
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        Log.e("DetailElderInfoViewModel", "어르신 정보 처리 실패: $exception")
                        _isSuccess.value = false
                        _errorMessage.value = "정보 수정을 실패했습니다. 다시 시도해주세요."
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteElderInfo(elderId: Int, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isDeleteSuccess.value = false
            try {
                eldersInfoRepository.deleteElder(elderId)
                    .onSuccess {
                        Log.d("DetailElderInfoViewModel", "어르신 정보 삭제 완료: $it")
                        _isDeleteSuccess.value = true
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        Log.e("DetailElderInfoViewModel", "어르신 정보 삭제 실패: $exception")
                        _errorMessage.value = "정보 삭제를 실패했습니다."
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetStatus() {
        _isUpdateSuccess.value = false
        _isDeleteSuccess.value = false
        _errorMessage.value = null
    }

    fun initializeForm(elderInfo: EldersInfoResponseDto) {
        _isMale.value = elderInfo.gender == GenderType.MALE
        _name.value = elderInfo.name
        _phoneNum.value = elderInfo.phone
        _relationship.value = elderInfo.relationship
        _residenceType.value = elderInfo.residenceType
        // birth는 yyyy-MM-dd 형식을 yyyyMMdd로 변환
        val birthFormatted = elderInfo.birthDate.replace("-", "")
        _birth.value = birthFormatted
    }

    fun updateIsMale(value: Boolean) {
        _isMale.value = value
    }

    fun updateName(value: String) {
        _name.value = value
    }

    fun updateBirth(value: String) {
        _birth.value = value
    }

    fun updatePhoneNum(value: String) {
        _phoneNum.value = value
    }

    fun updateRelationship(value: RelationshipType) {
        _relationship.value = value
    }

    fun updateResidenceType(value: ElderResidenceType) {
        _residenceType.value = value
    }

    fun setShowDeleteDialog(value: Boolean) {
        _showDeleteDialog.value = value
    }
}
