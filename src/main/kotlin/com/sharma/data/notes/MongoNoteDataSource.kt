package com.sharma.com.sharma.data.notes

import com.sharma.com.sharma.data.response.NoteResponse
import org.bson.types.ObjectId
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoNoteDataSource(db: CoroutineDatabase) : NotesDataSource {
    private val collection = db.getCollection<Note>()

    override suspend fun getNotesByCreatedBy(createdId: String): List<NoteResponse> {
        val notes = collection.find(Note::createdBy eq createdId).toList()

        return notes.map {
            NoteResponse(
                id = it.id.toString(),
                createdBy = it.createdBy,
                title = it.title,
                description = it.description
            )
        }
    }

    override suspend fun getNoteById(id: ObjectId): NoteResponse? {
        return null
    }

    override suspend fun getNotes(): List<NoteResponse> {
        val notes = collection.find().toList()
        return notes.map {
            NoteResponse(
                id = it.id.toString(),
                createdBy = it.createdBy,
                title = it.title,
                description = it.description
            )
        }
    }

    override suspend fun insertNote(note: Note): Boolean {
        return collection.insertOne(note).wasAcknowledged()
    }

    override suspend fun delete(id: ObjectId, createdId: String): Long {
        return collection.deleteOne(
            and(
                Note::id eq id,
                Note::createdBy eq createdId
            )
        ).deletedCount
    }
}