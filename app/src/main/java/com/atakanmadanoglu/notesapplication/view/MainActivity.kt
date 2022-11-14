package com.atakanmadanoglu.notesapplication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.atakanmadanoglu.notesapplication.ui.theme.NotesApplicationTheme
import com.atakanmadanoglu.notesapplication.view.MainScreen

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