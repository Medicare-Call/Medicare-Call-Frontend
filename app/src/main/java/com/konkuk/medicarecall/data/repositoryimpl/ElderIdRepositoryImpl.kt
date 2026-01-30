package com.konkuk.medicarecall.data.repositoryimpl

import android.content.Context
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.ElderIds
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.util.ElderIdsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

val Context.elderIdsDataStore by dataStore(
    fileName = "elderIds",
    serializer = ElderIdsSerializer,
)

@Single
class ElderIdRepositoryImpl(
    val context: Context,
) : ElderIdRepository {

    override suspend fun updateElderIds(elderIdMap: Map<Int, String>) {
        context.elderIdsDataStore.updateData { it.copy(elderIds = elderIdMap) }
    }

    override suspend fun updateElderId(elderId: Int, name: String) {
        context.elderIdsDataStore.updateData { it.copy(elderIds = it.elderIds.plus(elderId to name)) }
    }

    override suspend fun getElderIds(): Map<Int, String> {
        val preferences = context.elderIdsDataStore.data.map { it.elderIds }
        return preferences.first()
    }

    override suspend fun clearElderIds() {
        context.elderIdsDataStore.updateData {
            ElderIds(elderIds = emptyMap())
        }
    }
}
