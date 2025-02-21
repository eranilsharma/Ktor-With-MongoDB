package com.sharma

import com.sharma.com.sharma.data.car.CarDataSource
import com.sharma.com.sharma.data.car.MongoCarDataSource
import com.sharma.com.sharma.data.car.carRoutes
import com.sharma.com.sharma.data.jokes.JokeDataSource
import com.sharma.com.sharma.data.jokes.getJokes
import com.sharma.com.sharma.data.jokes.getJokesWithPagination
import com.sharma.com.sharma.data.jokes.jokeRoutes
import com.sharma.com.sharma.data.user.authenticate
import com.sharma.com.sharma.data.notes.deleteNote
import com.sharma.com.sharma.data.notes.getNotes
import com.sharma.com.sharma.data.notes.NotesDataSource
import com.sharma.com.sharma.data.notes.saveNote
import com.sharma.com.sharma.data.user.UserDataSource
import com.sharma.com.sharma.data.user.getSecretInfo
import com.sharma.com.sharma.security.token.TokenConfig
import com.sharma.com.sharma.security.token.TokenService
import com.sharma.com.sharma.data.user.signIn
import com.sharma.com.sharma.data.user.signUpRoute
import com.sharma.security.hashing.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    notesDataSource: NotesDataSource,
    jokeDataSource: JokeDataSource,
    carDataSource: CarDataSource
) {
    routing {
        signUpRoute(hashingService, userDataSource)
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        authenticate()
        getSecretInfo()
        saveNote(notesDataSource)
        getNotes(notesDataSource)
        deleteNote(notesDataSource)
        jokeRoutes(jokeDataSource)
        getJokes(jokeDataSource)
        getJokesWithPagination(jokeDataSource)
        carRoutes(carDataSource)
    }
}
