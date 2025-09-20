package com.konkuk.medicarecall.data.repository

interface ElderIdRepository {
    fun addElderId(name: String, id: Int)
    fun clearElderId()
    fun getElderIds(): List<Map<String, Int>>
    fun replaceAll(items: List<Map<String, Int>>)
}
