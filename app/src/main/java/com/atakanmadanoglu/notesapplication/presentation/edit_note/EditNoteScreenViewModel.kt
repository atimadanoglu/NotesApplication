package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.GetNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditNoteScreenViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase
): ViewModel() {

    private val _noteUI = MutableStateFlow<NoteUI?>(null)
    val noteUI: StateFlow<NoteUI?> get() = _noteUI

    fun getNoteById(
        id: Int
    ) = viewModelScope.launch {
        _noteUI.value = getNoteByIdUseCase.invoke(id).first()
    }

}