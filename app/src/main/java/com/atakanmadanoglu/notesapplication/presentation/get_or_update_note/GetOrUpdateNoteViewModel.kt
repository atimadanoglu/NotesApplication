package com.atakanmadanoglu.notesapplication.presentation.get_or_update_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.GetNoteByIdUseCase
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetOrUpdateNoteViewModel @Inject constructor(
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