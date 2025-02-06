package com.sharma.com.sharma.data.response

import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(
    val id:String,
    val title:String,
    val description:String,
    val createdBy:String
)
