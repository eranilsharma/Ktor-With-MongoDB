package com.sharma.com.sharma.data.request

import kotlinx.serialization.Serializable

@Serializable
data class JokeRequest(
    val category: String,
    val delivery: String?=null,
    val error: Boolean,
    val flags: Flags,
    val id: Long,
    val joke: String?=null,
    val lang: String,
    val safe: Boolean,
    val setup: String?=null,
    val type: String
)

@Serializable
data class Flags(
    val explicit: Boolean,
    val nsfw: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val religious: Boolean,
    val sexist: Boolean
)