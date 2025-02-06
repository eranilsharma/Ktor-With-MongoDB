package com.sharma.com.sharma.data.user

import com.sharma.com.sharma.data.request.LoginRequest
import com.sharma.com.sharma.data.request.SignUpRequest
import com.sharma.com.sharma.data.response.AuthResponse
import com.sharma.com.sharma.security.token.TokenClaim
import com.sharma.com.sharma.security.token.TokenConfig
import com.sharma.com.sharma.security.token.TokenService
import com.sharma.security.hashing.HashingService
import com.sharma.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.signUpRoute(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("register") {
        val request = kotlin.runCatching { call.receiveNullable<SignUpRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest,"Empty Request body")
            return@post
        }
        val areFieldsBlank = request.name.isBlank() || request.email.isBlank() || request.mobile.isBlank()
        val pswdSort = request.password.length < 8
        if (areFieldsBlank || pswdSort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            name = request.name,
            mobile = request.mobile,
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val isUserExist = userDataSource.getUserByUserName(email = request.email)
        isUserExist?.let {
            call.respond(HttpStatusCode.Conflict,"Email is already taken")
            return@post
        }

        val wasAcknowledged = userDataSource.insertUser(user)
        if (wasAcknowledged.not()) {
            call.respond(HttpStatusCode.Conflict,"Something went wrong")
            return@post
        }
        call.respond(HttpStatusCode.OK,"User registration successful")
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    config: TokenConfig
) {
    post("login") {
        val log = LoggerFactory.getLogger("SignUpAPI")
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest,"Empty Request body")
            return@post
        }
        val user = userDataSource.getUserByUserName(request.email)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "User not found")
            return@post
        }
        val validPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!validPassword) {
            call.respond(HttpStatusCode.Conflict, "Wrong password")
            return@post
        }
        val token = tokenService.generate(
            config = config,
            claims = arrayOf(
                TokenClaim(
                    name = "userId",
                    value = user.id.toString()
                )
            )
        )
        call.respond(
            HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}