package com.sharma.com.sharma.data.notes

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    @BsonId val id: ObjectId = ObjectId(),
    val title:String,
    val description:String,
    val createdBy:String
)
