package com.sharma.com.sharma.data.response

import kotlinx.serialization.Serializable


@Serializable
data class JokePaginationResponse(
    val status:Boolean,
    val page:Int,
    val size:Int,
    val totalPages:Long,
    val totalJokes:Long,
    val jokes:List<JokeResponse>
)
