package com.sharma.com.sharma.data.notes

import com.sharma.com.sharma.data.request.NoteRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId


fun Route.saveNote(
    notesDataSource: NotesDataSource
) {
    authenticate {
        post("note") {
            val request = kotlin.runCatching { call.receiveNullable<NoteRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Empty Request body")
                return@post
            }
            val principal = call.principal<JWTPrincipal>()
            val createdBy = principal?.getClaim("userId", String::class)
            createdBy?.let {
                val note = Note(
                    title = request.title,
                    description = request.description,
                    createdBy = it
                )
                val wasAcknowledged = notesDataSource.insertNote(note)
                if (wasAcknowledged.not()) {
                    call.respond(HttpStatusCode.Conflict, "Something went wrong")
                    return@post
                }
                call.respond(HttpStatusCode.OK, "Note added successfully")
                return@post
            } ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized, "Token expire")
                return@post
            }

        }
    }
}

fun Route.getNotes(notesDataSource: NotesDataSource) {
    authenticate {
        route("get-post") {
            get{
                val userId = call.queryParameters["userId"]
                userId?.let {
                    val notes = notesDataSource.getNotesByCreatedBy(userId)
                    notes?.let {
                        call.respond(HttpStatusCode.OK, notes)
                        return@get
                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "No note found")
                        return@get
                    }
                }?:run {
                    val notes = notesDataSource.getNotes()
                    notes?.let {
                        call.respond(HttpStatusCode.OK, notes)
                        return@get
                    }
                }
            }
        }
    }
}

fun Route.deleteNote(notesDataSource: NotesDataSource) {
    authenticate {
        delete("delete/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val id = call.parameters["id"]
            id?.let {
                val objectId = try {
                    ObjectId(it)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ObjectId format")
                    return@delete
                }
                userId?.let {
                    val response = notesDataSource.delete(objectId, userId)
                    if (response > 0) {
                        call.respond(HttpStatusCode.OK, "Note Deleted successfully")
                        return@delete
                    } else {
                        call.respond(HttpStatusCode.NotFound, "No note found")
                        return@delete
                    }
                }?: kotlin.run {
                    call.respond(HttpStatusCode.Unauthorized, "Token expired")
                    return@delete
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound, "Missing ID parameter")
                return@delete
            }
        }
    }
}