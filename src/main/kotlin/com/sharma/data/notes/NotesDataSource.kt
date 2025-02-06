package com.sharma.com.sharma.data.notes

import com.sharma.com.sharma.data.response.NoteResponse
import org.bson.types.ObjectId

interface NotesDataSource {
    suspend fun getNotesByCreatedBy(createdId:String): List<NoteResponse>?
    suspend fun getNoteById(id:ObjectId): NoteResponse?
    suspend fun getNotes(): List<NoteResponse>?
    suspend fun insertNote(note: Note):Boolean
    suspend fun delete(id: ObjectId,createdId: String):Long
}