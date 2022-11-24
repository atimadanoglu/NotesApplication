package com.atakanmadanoglu.notesapplication.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atakanmadanoglu.notesapplication.domain.GetNotesUseCase
import com.atakanmadanoglu.notesapplication.domain.model.NoteUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    getNotesUseCase: GetNotesUseCase
): ViewModel() {
    private val _notesState = MutableStateFlow(listOf<NoteUI>())
    val notesState: StateFlow<List<NoteUI>> get() = _notesState

    init {
        viewModelScope.launch {
            getNotesUseCase.invoke().collectLatest {
                _notesState.value = it
            }
        }
    }
}
