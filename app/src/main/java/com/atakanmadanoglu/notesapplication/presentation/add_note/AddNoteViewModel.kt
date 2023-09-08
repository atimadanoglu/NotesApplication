package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.usecases.AddNoteUseCase
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {
    private val _state = MutableStateFlow(AddNoteUIState())
    val state: StateFlow<AddNoteUIState> get() = _state

    fun onTitleChanged(newInput: String) = setTitle(newInput)
    fun onDescriptionChanged(newInput: String) = setDescription(newInput)
    fun onDoneIconClicked() {
        addNote(
            inputTitle = _state.value.title,
            inputDescription = _state.value.description
        )
    }

    private fun setTitle(newValue: String) {
        _state.update { it.copy(title = newValue) }
    }

    private fun setDescription(newValue: String) {
        _state.update { it.copy(description = newValue) }
    }

    private fun addNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        addNoteUseCase(
            inputTitle = inputTitle,
            inputDescription = inputDescription
        )
    }
}