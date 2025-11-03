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
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderHealthData
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import com.konkuk.medicarecall.ui.type.RelationshipType
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElderRegisterRepositoryImpl @Inject constructor(
    private val elderRegisterService: ElderRegisterService,
//    private val elderIdRepository: ElderIdRepository,
) : ElderRegisterRepository {
    private suspend fun postElder(elderData: ElderData): ElderRegisterResponseDto {
        val response = elderRegisterService.postElder(
            ElderRegisterRequestDto(
                name = elderData.name,
                birthDate = elderData.birthDate.formatAsDate(),
                gender = if (elderData.gender) GenderType.MALE else GenderType.FEMALE,
                phone = elderData.phoneNumber,
                relationship = RelationshipType.entries.find { it.displayName == elderData.relationship }!!,
                residenceType = ElderResidenceType.entries.find { it.displayName == elderData.livingType }!!,
            ),
        )
        if (response.isSuccessful) {
            return response.body() ?: error("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw HttpException(response)
        }
    }

    override suspend fun postElderHealthInfo(id: Int, elderHealthData: ElderHealthData) {
        val response = elderRegisterService.postElderHealthInfo(
            id,
            ElderHealthRegisterRequestDto(
                diseaseNames = elderHealthData.diseaseNames,
                medicationSchedules = ElderHealthMapper.toRequestSchedules(elderHealthData.medicationMap),
                notes = elderHealthData.notes.map { notes ->
                    HealthIssueType.entries.find { it.displayName == notes }!!
                },
            ),
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw HttpException(response)
        }
    }

    override suspend fun postElderBulk(elderList: List<ElderData>): Result<ElderBulkRegisterResponseDto> = runCatching {
        val response = elderRegisterService.postElderBulk(
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
        )
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun postElderHealthInfoBulk(elderHealthList: List<ElderHealthData>): Result<Unit> = runCatching {
        val response = elderRegisterService.postElderHealthInfoBulk(
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
        )
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
}
