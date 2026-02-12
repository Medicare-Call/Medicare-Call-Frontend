package com.konkuk.medicarecall.data.repositoryimpl

import androidx.datastore.core.DataStore
import com.konkuk.medicarecall.data.di.ElderIdDataStore
import com.konkuk.medicarecall.data.model.ElderIds
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ElderIdRepositoryImpl(
    @ElderIdDataStore private val elderIdDataStore: DataStore<ElderIds>,
) : ElderIdRepository {

    override suspend fun updateElderIds(elderIdMap: Map<Long, String>) {
        elderIdDataStore.updateData { it.copy(elderIds = elderIdMap) }
    }

    override suspend fun updateElderId(elderId: Long, name: String) {
        elderIdDataStore.updateData { it.copy(elderIds = it.elderIds.plus(elderId to name)) }
    }

    override suspend fun getElderIds(): Map<Long, String> {
        val preferences = elderIdDataStore.data.map { it.elderIds }
        return preferences.first()
    }

    override suspend fun clearElderIds() {
        elderIdDataStore.updateData {
            ElderIds(elderIds = emptyMap())
        }
    }
}
