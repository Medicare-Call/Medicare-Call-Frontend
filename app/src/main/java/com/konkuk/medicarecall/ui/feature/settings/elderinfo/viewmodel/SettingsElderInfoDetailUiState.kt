package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import com.konkuk.medicarecall.ui.model.ElderInfo
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.RelationshipType

data class SettingsElderInfoDetailUiState(
    val elderData: ElderInfo? = null,
    val isMale: Boolean = false,
    val name: String = "",
    val birth: String = "",
    val phoneNum: String = "",
    val relationship: RelationshipType = RelationshipType.ACQUAINTANCE,
    val residenceType: ElderResidenceType = ElderResidenceType.WITH_FAMILY,
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
