package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.Relationship

data class SettingsElderInfoDetailUiState(
    val elderData: ElderInfo? = null,
    val isMale: Boolean = false,
    val name: String = "",
    val birth: String = "",
    val phoneNum: String = "",
    val relationship: Relationship = Relationship.ACQUAINTANCE,
    val residenceType: ElderResidence = ElderResidence.WITH_FAMILY,
    val showDeleteDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val isDeleteSuccess: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasData: Boolean get() = elderData != null
    val hasError: Boolean get() = errorMessage != null
}
