package com.atakanmadanoglu.notesapplication.data.repository.notes

import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.di.IoDispatcher
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(
    private val notesDao: NotesDao,
    private val noteMapper: NoteEntityMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NotesRepository {
    override fun getNotesByCreatedAt(): Flow<List<NoteDomain>> =
        notesDao.getNotesByCreatedAt().map { notesList ->
            notesList.map {
                noteMapper.mapToNoteDomain(it)
            }
        }

    override fun getNoteById(id: Int): Flow<NoteDomain> =
        notesDao.getNoteById(id).map {
            noteMapper.mapToNoteDomain(it)
        }.flowOn(ioDispatcher)

    override suspend fun addNote(noteDomain: NoteDomain) = withContext(ioDispatcher) {
        val noteEntity = noteMapper.mapToNoteEntity(noteDomain)
        notesDao.addNote(noteEntity)
    }

    override suspend fun editNote(noteDomain: NoteDomain) = withContext(ioDispatcher) {
        val noteEntity = noteMapper.mapToNoteEntity(noteDomain)
        notesDao.editNote(noteEntity)
    }

    override suspend fun deleteNoteById(noteId: Int) = withContext(ioDispatcher) {
        notesDao.deleteNoteById(noteId)
    }

    override suspend fun deleteNotesByIds(noteIds: List<Int>) {
        notesDao.deleteNotesByIds(noteIds)
    }
}