package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.repository.ElderIdRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElderIdRepositoryImpl @Inject constructor() : ElderIdRepository {

    private val elderIds: MutableList<Map<String, Int>> = mutableListOf()

    override fun addElderId(name: String, id: Int) {
        this.elderIds.add(mapOf(name to id))
    }

    override fun clearElderId() {
        elderIds.clear()
    }

    override fun getElderIds(): List<Map<String, Int>> {
        return elderIds
    }

    override fun replaceAll(items: List<Map<String, Int>>) {
        elderIds.clear()
        elderIds.addAll(items)
    }
}
