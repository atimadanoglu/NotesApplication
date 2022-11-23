package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(
    private val notesDao: NotesDao,
    private val noteMapper: NoteEntityMapper
) : NotesRepository {
    override fun getNotes(): Flow<List<Note>> =
        notesDao.getNotes().map { notesList ->
            notesList.map {
                noteMapper.mapToNote(it)
            }
        }

    override fun getNoteById(id: Int): Flow<Note> =
        notesDao.getNoteById(id).map {
            noteMapper.mapToNote(it)
        }

    override suspend fun addNote(addNoteRequest: AddNoteRequest) {
        val noteEntity = noteMapper.mapToEntity(addNoteRequest)
        notesDao.addNote(noteEntity)
    }
}