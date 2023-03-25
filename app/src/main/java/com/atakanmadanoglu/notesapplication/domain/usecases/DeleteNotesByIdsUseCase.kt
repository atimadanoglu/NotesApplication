package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.data.repository.notes.NotesRepository
import javax.inject.Inject

class DeleteNotesByIdsUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteIds: List<Int>) {
        notesRepository.deleteNotesByIds(noteIds)
    }
}