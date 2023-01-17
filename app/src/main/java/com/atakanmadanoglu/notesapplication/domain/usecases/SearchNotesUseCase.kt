package com.atakanmadanoglu.notesapplication.domain.usecases

import com.atakanmadanoglu.notesapplication.di.DefaultDispatcher
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        allNotesList: List<NoteUI>,
        searchText: String
    ) = withContext(defaultDispatcher) {
        allNotesList.filter { noteUI ->
            noteUI.title.contains(searchText, ignoreCase = true) ||
                    noteUI.description.contains(searchText, ignoreCase = true)
        }
    }
}