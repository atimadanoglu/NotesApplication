package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(
    private val notesDao: NotesDao,
    private val noteMapper: NoteEntityMapper
) : NotesRepository {
    override fun getNotes(): Flow<List<NoteDomain>> =
        notesDao.getNotes().map { notesList ->
            notesList.map {
                noteMapper.mapToNoteDomain(it)
            }
        }

    override fun getNoteById(id: Int): Flow<NoteDomain> =
        notesDao.getNoteById(id).map {
            noteMapper.mapToNoteDomain(it)
        }

    override suspend fun addNote(noteDomain: NoteDomain) {
        val noteEntity = noteMapper.mapToNoteEntity(noteDomain)
        notesDao.addNote(noteEntity)
    }
}