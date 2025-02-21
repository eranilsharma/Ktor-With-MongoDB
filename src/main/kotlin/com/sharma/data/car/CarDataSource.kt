package com.sharma.com.sharma.data.car

interface CarDataSource {
    suspend fun create(car: Car): Boolean
    suspend fun read(id: String): CarResponse?
    suspend fun update(id: String, car: CarRequest): Boolean
    suspend fun delete(id: String): Boolean
}