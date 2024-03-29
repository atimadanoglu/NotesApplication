package com.atakanmadanoglu.notesapplication.data.repository.notes

import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesByCreatedAt(): Flow<List<NoteDomain>>
    fun getNoteById(id: Int): Flow<NoteDomain>
    suspend fun addNote(noteDomain: NoteDomain)
    suspend fun editNote(noteDomain: NoteDomain)
    suspend fun deleteNoteById(noteId: Int)
    suspend fun deleteNotesByIds(noteIds: List<Int>)
}