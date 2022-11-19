package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note>
    suspend fun addNote(note: Note)
}