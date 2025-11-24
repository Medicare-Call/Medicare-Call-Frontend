package com.konkuk.medicarecall.data.repositoryimpl

import android.content.Context
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.ElderIds
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.util.ElderIdsSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.elderIdsDataStore by dataStore(
    fileName = "elderIds",
    serializer = ElderIdsSerializer,
)

@Singleton
class ElderIdRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : ElderIdRepository {

    override suspend fun updateElderIds(elderIdMap: Map<Int, String>) {
        context.elderIdsDataStore.updateData { it.copy(elderIds = elderIdMap) }
    }

    override suspend fun updateElderId(elderId: Int, name: String) {
        val elderIds = getElderIds().first().toMutableMap()
        elderIds[elderId] = name
        context.elderIdsDataStore.updateData { it.copy(elderIds = elderIds) }
    }

    override suspend fun getElderIds(): Flow<Map<Int, String>> {
        val preferences = context.elderIdsDataStore.data.map { it.elderIds }
        return preferences
    }

    override suspend fun clearElderIds() {
        context.elderIdsDataStore.updateData {
            ElderIds(elderIds = emptyMap())
        }
    }

}
