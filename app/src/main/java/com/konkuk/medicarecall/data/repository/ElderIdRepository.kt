package com.konkuk.medicarecall.data.repository

interface ElderIdRepository {
    suspend fun updateElderIds(elderIdMap: Map<Int, String>)
    suspend fun updateElderId(elderId: Int, name: String)
    suspend fun clearElderIds()
    suspend fun getElderIds(): Map<Int, String>
}
