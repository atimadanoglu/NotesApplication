package com.atakanmadanoglu.notesapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.atakanmadanoglu.notesapplication.presentation.add_note.AddNoteRoute
import com.atakanmadanoglu.notesapplication.presentation.edit_note.EditNoteRoute
import com.atakanmadanoglu.notesapplication.presentation.navigation.Route
import com.atakanmadanoglu.notesapplication.presentation.notes_list.NoteListRoute
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
                        startDestination = Route.NoteList
                    ) {
                        composable<Route.NoteList> {
                            NoteListRoute(
                                addNoteButtonClicked = { navController.navigate(Route.AddNote) },
                                onCardClicked = { id -> navController.navigate(Route.EditNote(id)) }
                            )
                        }

                        composable<Route.AddNote> {
                            AddNoteRoute(navController)
                        }

                        composable<Route.EditNote> {
                            val args = it.toRoute<Route.EditNote>()
                            EditNoteRoute(id = args.id, navController = navController)
                        }
                    }
                }
            }
        }
    }
}