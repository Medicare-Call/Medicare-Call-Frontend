package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.RelationshipType

data class SettingsElderInfoDetailUiState(
    val elderData: EldersInfoResponseDto? = null,
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
)
