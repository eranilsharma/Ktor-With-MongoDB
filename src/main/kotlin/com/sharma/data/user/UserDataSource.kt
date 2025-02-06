package com.sharma.com.sharma.data.user


interface UserDataSource {
    suspend fun getUserByUserName(email:String): User?
    suspend fun insertUser(user: User):Boolean
}