package com.sharma.com.sharma.data.car

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


data class Car(
    @BsonId val id: ObjectId = ObjectId(),
    val brandName: String,
    val model: String,
    val number: String
)

@Serializable
data class CarRequest(
    val brandName: String,
    val model: String,
    val number: String
)

@Serializable
data class CarResponse(
    val id: String,
    val brandName: String,
    val model: String,
    val number: String
)
