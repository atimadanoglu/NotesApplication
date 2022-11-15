package com.atakanmadanoglu.notesapplication.data.local

import com.atakanmadanoglu.notesapplication.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesLocalDataSource @Inject constructor(
    private val dao: NotesDao
) {
    fun getNotes(): Flow<List<Note>> = dao.getNotes()
    suspend fun getNoteById(id: Int): Note = dao.getNoteById(id)
    suspend fun addNote(note: Note) = dao.addNote(note)
}