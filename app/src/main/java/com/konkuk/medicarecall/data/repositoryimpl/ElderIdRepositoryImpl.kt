package com.konkuk.medicarecall.data.repositoryimpl

import android.content.Context
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.ElderIds
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import org.koin.core.annotation.Single

val Context.elderIdsDataStore by dataStore(
    fileName = "elderIds",
    serializer = ElderIdsSerializer,
)


@Single
class ElderIdRepositoryImpl : ElderIdRepository {

    override suspend fun updateElderIds(elderIdMap: Map<Int, String>) {
        context.elderIdsDataStore.updateData { it.copy(elderIds = elderIdMap) }
    }

    override suspend fun updateElderId(elderId: Int, name: String) {
        val elderIds = getElderIds().first().toMutableMap()
        elderIds[elderId] = name
        context.elderIdsDataStore.updateData { it.copy(elderIds = elderIds) }
    }

    override fun getElderIds(): Flow<Map<Int, String>> {
        val preferences = context.elderIdsDataStore.data.map { it.elderIds }
        return preferences
    } // Flow 자체가 비동기 스트림인데 suspend를 또 붙였다 해서 수정했습니다. 문제 시 다시 원상복구 해놓겠습니다

    override suspend fun clearElderIds() {
        context.elderIdsDataStore.updateData {
            ElderIds(elderIds = emptyMap())
        }
    }

}
