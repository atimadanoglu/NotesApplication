package com.atakanmadanoglu.notesapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.atakanmadanoglu.notesapplication.presentation.add_note.navigation.addNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.add_note.navigation.navigateToAddNote
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.editNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.edit_note.navigation.navigateToEditNoteScreen
import com.atakanmadanoglu.notesapplication.presentation.navigation.Screen
import com.atakanmadanoglu.notesapplication.presentation.notes_list.navigation.noteListScreen
import com.atakanmadanoglu.notesapplication.theme.NotesApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme(
                dynamicColor = false
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NOTE_LIST.route
                    ) {
                        noteListScreen(
                            navigateToAddNoteScreen = navController::navigateToAddNote,
                            navigateToEditNoteScreen = navController::navigateToEditNoteScreen
                        )
                        editNoteScreen(navController)
                        addNoteScreen(navController)
                    }
                }
            }
        }
    }
}