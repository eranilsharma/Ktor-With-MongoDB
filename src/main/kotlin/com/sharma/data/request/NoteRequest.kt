package com.sharma.com.sharma.data.request

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title:String,
    val description:String
)
