package com.atakanmadanoglu.notesapplication.domain

import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.mapper.NoteUIMapper
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val noteMapper: NoteUIMapper
) {
    operator fun invoke() : Flow<List<NoteUI>> {
        return notesRepository.getNotes().map { noteList ->
            noteList.map { note ->
                noteMapper.mapToNoteUI(note)
            }
        }
    }
}