package com.atakanmadanoglu.notesapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.atakanmadanoglu.notesapplication.presentation.main_screen.MainScreen
import com.atakanmadanoglu.notesapplication.ui.theme.NotesApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    //AddNoteScreen()
                    MainScreen()
                }
            }
        }
    }
}