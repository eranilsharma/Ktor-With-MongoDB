package com.sharma.com.sharma.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class MongoUserDataSource(db: CoroutineDatabase) : UserDataSource {
    private val users = db.getCollection<User>()
    override suspend fun getUserByUserName(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}