package com.sharma.com.sharma.data.car

import ch.qos.logback.classic.Logger
import com.sharma.com.sharma.data.request.NoteRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*


fun Route.carRoutes(service: CarDataSource) {
    val logger = KotlinLogging.logger {}
    route("/cars") {
        post {
            val carRequest = kotlin.runCatching { call.receiveNullable<CarRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Empty Request body")
                return@post
            }

            val car = Car(
                brandName = carRequest.brandName,
                model = carRequest.model,
                number = carRequest.number
            )
            val result = service.create(car)
            if (result) call.respond(HttpStatusCode.Created, "Car details inserted successfully")
            else {
                call.respond(HttpStatusCode.Conflict, "Car details already found")
            }
        }

        get("/{id}") {
            val id =
                call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Please enter valid id")
            val car = service.read(id) ?: return@get call.respond(HttpStatusCode.NotFound,"Car not found")
            call.respond(car)
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val car = call.receive<CarRequest>()
            val updated = service.update(id, car)
            if (updated) call.respond(HttpStatusCode.OK,"Car details updated successfully") else call.respond(HttpStatusCode.Conflict,"Car id mismatch")
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deleted = service.delete(id)
            if (deleted) call.respond(HttpStatusCode.OK,"Car details deleted successfully") else call.respond(HttpStatusCode.NotFound,"Car not found")
        }
    }
}