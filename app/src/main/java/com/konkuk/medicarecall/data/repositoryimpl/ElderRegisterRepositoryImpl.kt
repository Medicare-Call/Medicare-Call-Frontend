package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderHealthRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.ElderBulkRegisterResponseDto
import com.konkuk.medicarecall.data.dto.response.ElderRegisterResponseDto
import com.konkuk.medicarecall.data.mapper.ElderHealthMapper
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderHealthData
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import com.konkuk.medicarecall.ui.type.RelationshipType
import org.koin.core.annotation.Single

@Single
class ElderRegisterRepositoryImpl(
    private val elderRegisterService: ElderRegisterService,
) : ElderRegisterRepository {
    private suspend fun postElder(elderData: ElderData): ElderRegisterResponseDto =
        elderRegisterService.postElder(
            ElderRegisterRequestDto(
                name = elderData.name,
                birthDate = elderData.birthDate.formatAsDate(),
                gender = if (elderData.gender) GenderType.MALE else GenderType.FEMALE,
                phone = elderData.phoneNumber,
                relationship = RelationshipType.entries.find { it.displayName == elderData.relationship }!!,
                residenceType = ElderResidenceType.entries.find { it.displayName == elderData.livingType }!!,
            ),
        ).handleResponse()

    override suspend fun postElderHealthInfo(id: Int, elderHealthData: ElderHealthData) {
        elderRegisterService.postElderHealthInfo(
            id,
            ElderHealthRegisterRequestDto(
                diseaseNames = elderHealthData.diseaseNames,
                medicationSchedules = ElderHealthMapper.toRequestSchedules(elderHealthData.medicationMap),
                notes = elderHealthData.notes.map { notes ->
                    HealthIssueType.entries.find { it.displayName == notes }!!
                },
            ),
        ).handleNullableResponse()
    }

    override suspend fun postElderBulk(elderList: List<ElderData>): Result<ElderBulkRegisterResponseDto> = runCatching {
        elderRegisterService.postElderBulk(
            ElderBulkRegisterRequestDto(
                elders = elderList.map { elderData ->
                    ElderBulkRegisterRequestDto.ElderInfo(
                        name = elderData.name,
                        birthDate = elderData.birthDate.formatAsDate(),
                        gender = if (elderData.gender) GenderType.MALE.name else GenderType.FEMALE.name,
                        phone = elderData.phoneNumber,
                        relationship = RelationshipType.entries.find { it.displayName == elderData.relationship }!!.name,
                        residenceType = ElderResidenceType.entries.find { it.displayName == elderData.livingType }!!.name,
                    )
                },
            ),
        ).handleResponse()
    }

    override suspend fun postElderHealthInfoBulk(elderHealthList: List<ElderHealthData>): Result<Unit> = runCatching {
        elderRegisterService.postElderHealthInfoBulk(
            ElderBulkHealthInfoRequestDto(
                healthInfos = elderHealthList.map { elderHealthData ->
                    ElderBulkHealthInfoRequestDto.HealthInfo(
                        elderId = elderHealthData.id!!,
                        diseaseNames = elderHealthData.diseaseNames,
                        medicationSchedules = ElderHealthMapper.toRequestSchedules(elderHealthData.medicationMap).map { schedule ->
                            ElderBulkHealthInfoRequestDto.HealthInfo.MedicationSchedule(
                                medicationName = schedule.medicationName,
                                scheduleTimes = schedule.scheduleTimes.map { it.name },
                            )
                        },
                        notes = elderHealthData.notes.map { note ->
                            HealthIssueType.entries.find { it.displayName == note }!!.name
                        },
                    )
                },
            ),
        ).handleNullableResponse()
    }
}
