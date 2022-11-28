package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.mapper.NoteUIMapper
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val noteUIMapper: NoteUIMapper
) {
    operator fun invoke(
        id: Int
    ): Flow<NoteUI> = notesRepository
        .getNoteById(id).map {
            noteUIMapper.mapToNoteUI(it)
        }
}