package com.sharma.com.sharma.data.car

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoCarDataSource(database: CoroutineDatabase) : CarDataSource {
    private val collection: CoroutineCollection<Car> = database.getCollection()

    override suspend fun create(car: Car): Boolean {
        // Check if a car with the same number already exists
        val existingCar = collection.findOne(Car::number eq car.number)
        if (existingCar == null) {
            val result = collection.insertOne(car)
            return result.wasAcknowledged()
        } else return false
    }

    override suspend fun read(id: String): CarResponse? {
        val result = collection.findOneById(ObjectId(id))
        return result?.let {
            CarResponse(
                id = it.id.toString(),
                model = it.model,
                brandName = it.brandName,
                number = it.number
            )
        } ?: return null
    }

    override suspend fun update(id: String, car: CarRequest): Boolean {
        val existingCar = collection.findOne(Car::number eq car.number)
        if (existingCar != null) {
            if(id == existingCar.id.toString()) {
                val result = collection.replaceOneById(ObjectId(id), car)
                return result.modifiedCount > 0
            }
            else return false
        }
        else return false
    }

    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOneById(ObjectId(id))
        return result.deletedCount > 0
    }

}