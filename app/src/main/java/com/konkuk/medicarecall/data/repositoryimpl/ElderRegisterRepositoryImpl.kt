package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.mapper.ElderHealthMapper
import com.konkuk.medicarecall.data.mapper.toElderBulkRequestDto
import com.konkuk.medicarecall.data.mapper.toModel
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData
import com.konkuk.medicarecall.ui.type.HealthIssueType
import org.koin.core.annotation.Single

@Single
class ElderRegisterRepositoryImpl(
    private val elderRegisterService: ElderRegisterService,
) : ElderRegisterRepository {

    override suspend fun postElderBulk(elderList: List<LoginElderData>): Result<List<Elder>> = runCatching {
        val request = ElderBulkRegisterRequestDto(
            elders = elderList.map { it.toModel().toElderBulkRequestDto() },
        )
        elderRegisterService.postElderBulk(request).handleResponse().toModel()
    }

    override suspend fun postElderHealthInfoBulk(elderHealthList: List<LoginElderData>): Result<Unit> = runCatching {
        elderRegisterService.postElderHealthInfoBulk(
            ElderBulkHealthInfoRequestDto(
                healthInfos = elderHealthList.map { elderData ->
                    ElderBulkHealthInfoRequestDto.HealthInfo(
                        elderId = elderData.id.toInt(),
                        diseaseNames = elderData.diseases,
                        medicationSchedules = ElderHealthMapper.toRequestSchedules(elderData.medicationMap).map { schedule ->
                            ElderBulkHealthInfoRequestDto.HealthInfo.MedicationSchedule(
                                medicationName = schedule.medicationName,
                                scheduleTimes = schedule.scheduleTimes.map { it.name },
                            )
                        },
                        notes = elderData.notes.map { note ->
                            HealthIssueType.entries.find { it.displayName == note.displayName }!!.name
                        },
                    )
                },
            ),
        ).handleNullableResponse()
    }
}
