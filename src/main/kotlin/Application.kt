package com.sharma

import com.sharma.com.sharma.data.car.MongoCarDataSource
import com.sharma.com.sharma.data.jokes.MongoJokeDataSource
import com.sharma.com.sharma.data.notes.MongoNoteDataSource
import com.sharma.com.sharma.data.user.MongoUserDataSource
import com.sharma.com.sharma.security.token.JwtTokenService
import com.sharma.com.sharma.security.token.TokenConfig
import com.sharma.security.hashing.SHA256HashingService
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val MONGO_PW = System.getenv("MONGO_PW")
    val dbName = "ktor"
    val db =
        KMongo.createClient(connectionString = "mongodb+srv://eranilsharma:$MONGO_PW@cluster0.8afbj.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
            .coroutine.getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val notesDataSource = MongoNoteDataSource(db)
    val jokeDataSource = MongoJokeDataSource(db)
    val carDataSource = MongoCarDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 1000L * 60L * 30L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(
        userDataSource,
        hashingService,
        tokenService,
        tokenConfig,
        notesDataSource,
        jokeDataSource,
        carDataSource
    )
}
