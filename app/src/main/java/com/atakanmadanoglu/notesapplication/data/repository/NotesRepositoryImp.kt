package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteMapper
import com.atakanmadanoglu.notesapplication.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(
    private val notesDao: NotesDao,
    private val noteMapper: NoteMapper
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

    override suspend fun addNote(note: Note) {
        val noteEntity = noteMapper.mapToEntity(note)
        notesDao.addNote(noteEntity)
    }
}