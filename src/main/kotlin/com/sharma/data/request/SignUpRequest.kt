package com.sharma.com.sharma.data.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val name:String,
    val email:String,
    val mobile:String,
    val password:String
)
