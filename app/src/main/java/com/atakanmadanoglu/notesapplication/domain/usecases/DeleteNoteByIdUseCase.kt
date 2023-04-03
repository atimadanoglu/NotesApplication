package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.data.repository.notes.NotesRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int) {
        notesRepository.deleteNoteById(noteId)
    }
}