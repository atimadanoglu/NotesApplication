package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note>
    suspend fun addNote(addNoteRequest: AddNoteRequest)
}