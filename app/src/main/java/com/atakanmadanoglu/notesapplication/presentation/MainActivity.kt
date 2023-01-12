package com.atakanmadanoglu.notesapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.add_note.navigation.addNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.add_note.navigation.navigateToAddNote
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.editNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.navigateToEditNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen
import com.atakanmadanoglu.notesapplication.presentation.notes_list.navigation.notesList
import com.atakanmadanoglu.notesapplication.ui.theme.NotesApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // to specify which icon will be displayed at first
        chooseThemeIcon(viewModel.mainActivityUiState.isDarkTheme)
        setContent {
            NotesApplicationTheme(
                darkTheme = viewModel.mainActivityUiState.isDarkTheme,
                dynamicColor = false
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val state by remember {
                        mutableStateOf(viewModel.mainActivityUiState)
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesListScreen.route
                    ) {
                        notesList(
                            navigateToAddNoteScreen = {
                                navController.navigateToAddNote()
                            },
                            navigateToEditNoteScreen = { noteId ->
                                navController.navigateToEditNoteScreen(noteId)
                            },
                            setDarkTheme = {
                                val isDarkTheme = !state.isDarkTheme
                                viewModel.updateActivityTheme(isDarkTheme)
                                chooseThemeIcon(state.isDarkTheme)
                            },
                            iconId = state.themeIconId
                        )
                        editNoteScreen(navController)
                        addNoteScreen(navController)
                    }
                }
            }
        }
    }

    private fun chooseThemeIcon(isDark: Boolean) {
        if (isDark) {
            viewModel.updateThemeIcon(R.drawable.ic_outline_light_mode_24)
        } else {
            viewModel.updateThemeIcon(R.drawable.ic_outline_dark_mode_24)
        }
    }
}

@Stable
interface MainActivityUiState {
    val isDarkTheme: Boolean
    val themeIconId: Int
}

class MutableMainActivityUiState: MainActivityUiState {
    override var isDarkTheme by mutableStateOf(true)
    override var themeIconId by mutableStateOf(0)
}

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val _mainActivityUiState = MutableMainActivityUiState()
    val mainActivityUiState: MainActivityUiState = _mainActivityUiState

    fun updateActivityTheme(value: Boolean) {
        _mainActivityUiState.isDarkTheme = value
    }

    fun updateThemeIcon(id: Int) {
        _mainActivityUiState.themeIconId = id
    }
}