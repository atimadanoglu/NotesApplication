package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.data.local.NotesLocalDataSource
import com.atakanmadanoglu.notesapplication.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(
    private val notesLocalDataSource: NotesLocalDataSource
) : NotesRepository {
    override fun getNotes(): Flow<List<Note>> {
        return notesLocalDataSource.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note {
        return notesLocalDataSource.getNoteById(id)
    }

    override suspend fun addNote(note: Note) {
        notesLocalDataSource.addNote(note)
    }
}