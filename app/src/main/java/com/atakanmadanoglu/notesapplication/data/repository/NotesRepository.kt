package com.atakanmadanoglu.notesapplication.data.repository

import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesByCreatedAt(): Flow<List<NoteDomain>>
    fun getNoteById(id: Int): Flow<NoteDomain>
    suspend fun addNote(noteDomain: NoteDomain)
    suspend fun editNote(noteDomain: NoteDomain)
}