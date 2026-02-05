package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.mapper.ElderHealthMapper
import com.konkuk.medicarecall.data.mapper.toModel
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.HealthIssueType
import org.koin.core.annotation.Single

@Single
class ElderRegisterRepositoryImpl(
    private val elderRegisterService: ElderRegisterService,
) : ElderRegisterRepository {

    override suspend fun postElderBulk(elderList: List<LoginElderData>): Result<List<Elder>> = runCatching {
        elderRegisterService.postElderBulk(
            ElderBulkRegisterRequestDto(
                elders = elderList.map { elderData ->
                    ElderBulkRegisterRequestDto.ElderInfo(
                        name = elderData.nameState.text.toString(),
                        birthDate = elderData.birthDateState.text.toString().formatAsDate(),
                        gender = if (elderData.gender == GenderType.MALE) GenderType.MALE.name else GenderType.FEMALE.name,
                        phone = elderData.phoneNumberState.text.toString(),
                        relationship = elderData.relationship!!.name,
                        residenceType = elderData.livingType!!.name,
                    )
                },
            ),
        ).handleResponse().toModel()
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
                            HealthIssueType.entries.find { it.displayName == note }!!.name
                        },
                    )
                },
            ),
        ).handleNullableResponse()
    }
}
