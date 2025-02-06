package com.sharma.com.sharma.data.jokes

import com.sharma.com.sharma.data.request.JokeRequest
import com.sharma.com.sharma.data.response.JokeResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Initialize Ktor HTTP Client
val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

fun Route.jokeRoutes(jokeDataSource: JokeDataSource) {
    authenticate {
        post("/fetch-joke") {
            try {
                // ✅ Step 1: Fetch a joke from the JokeAPI
                val apiResponse: JokeRequest = client.get("https://jokeapi-v2.p.rapidapi.com/joke/Any") {
                    headers {
                        append("x-rapidapi-host", "jokeapi-v2.p.rapidapi.com")
                        append(
                            "x-rapidapi-key",
                            "266f1bda34msh1f5ddd58fd6323ap1f6e20jsn66262260b78d"
                        )
                    }
                }.body()

                // ✅ Step 2: Save the joke in MongoDB
                val newJoke = Joke(
                    type = apiResponse.type,
                    setup = apiResponse.setup,
                    delivery = apiResponse.delivery,
                    joke = apiResponse.joke,
                    jokeId = apiResponse.id
                )

                val result = jokeDataSource.addNewJoke(newJoke)

                // ✅ Step 3: Return the saved joke

                call.respond(
                    if (result) HttpStatusCode.Created else HttpStatusCode.OK, JokeResponse(
                        type = apiResponse.type,
                        setup = apiResponse.setup,
                        delivery = apiResponse.delivery,
                        joke = apiResponse.joke,
                        jokeId = apiResponse.id
                    )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error fetching joke: ${e.message}")
            }
        }
    }
}

fun Route.getJokes(jokeDataSource: JokeDataSource) {
    authenticate {
        get("getAllJokes") {
            val list = jokeDataSource.getAllJokes()
            list?.let {
                call.respond(HttpStatusCode.OK, list)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.Conflict, "No Data found")
            }
        }
    }
}

fun Route.getJokesWithPagination(jokeDataSource: JokeDataSource) {
    authenticate {
        get("getJokes") {
            try {
                // ✅ Read pagination parameters from query parameters
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10

                if (page < 1 || size < 1) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid page or size values")
                    return@get
                }
                val response = jokeDataSource.getJokesWithPagination(page, size)
                call.respond(HttpStatusCode.OK, response)
                return@get

            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error:${e.message}")
                return@get
            }
        }
    }
}
