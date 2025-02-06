package com.sharma.com.sharma.data.response

import kotlinx.serialization.Serializable

@Serializable
data class JokeResponse(
    val type: String,
    val setup: String? = null,
    val delivery: String? = null,
    val joke: String? = null,
    val jokeId :Long
)