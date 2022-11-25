package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.AddNoteUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {
    fun addNote(
        addNoteRequest: AddNoteRequest
    ) = viewModelScope.launch {
        addNoteUseCase.invoke(addNoteRequest)
    }
}