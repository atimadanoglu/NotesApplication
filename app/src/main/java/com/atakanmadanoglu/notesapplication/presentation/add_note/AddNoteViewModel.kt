package com.atakanmadanoglu.notesapplication.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.AddNoteUseCase
import com.atakanmadanoglu.notesapplication.presentation.mapper.AddNoteRequestMapper
import com.atakanmadanoglu.notesapplication.presentation.state.AddNoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val addNoteRequestMapper: AddNoteRequestMapper
): ViewModel() {

    private val _addNoteState = MutableStateFlow(AddNoteState())
    val addNoteState = _addNoteState.asStateFlow()

    fun addNote(
        inputTitle: String,
        inputDescription: String
    ) = viewModelScope.launch {
        with(_addNoteState.value) {
            createdAt = Date().time
            title = inputTitle
            description = inputDescription
            addNoteUseCase.invoke(
                addNoteRequestMapper.mapToAddNoteRequest(this)
            )
        }
    }
}