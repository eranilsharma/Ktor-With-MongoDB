package com.sharma.com.sharma.data.jokes

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Joke(
    @BsonId val id: ObjectId = ObjectId(),
    val type: String,
    val setup: String? = null,
    val delivery: String? = null,
    val joke: String? = null,
    val jokeId :Long
)