package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.add_note.NavigationTopAppBar
import com.atakanmadanoglu.notesapplication.presentation.add_note.NoteContentView
import com.atakanmadanoglu.notesapplication.presentation.add_note.TitleInput
import com.atakanmadanoglu.notesapplication.presentation.model.EditNoteUiState
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.spacing

@Composable
internal fun EditNoteRoute(
    navController: NavController,
    editNoteScreenViewModel: EditNoteScreenViewModel = hiltViewModel()
) {
    val editNoteUiState by editNoteScreenViewModel.editNoteUiState.collectAsStateWithLifecycle()

    EditNoteScreen(
        navController = navController,
        editNoteUiState = editNoteUiState,
        navigationIconOnClick = navController::popBackStack,
        whenIsFocused = { editNoteScreenViewModel.updateFocusValue(true) },
        whenNotHaveFocus = { editNoteScreenViewModel.updateFocusValue(false) },
        titleOnChange = { editNoteScreenViewModel.updateTitleValue(it) },
        descriptionOnChange = { editNoteScreenViewModel.updateDescriptionValue(it) },
        onDeleteClicked = editNoteScreenViewModel::deleteNote,
        doneIconOnClick = {
            editNoteScreenViewModel.editNote(
                inputTitle = editNoteUiState.title,
                inputDescription = editNoteUiState.description
            )
        }
    )
}

@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    editNoteUiState: EditNoteUiState,
    doneIconOnClick: () -> Unit,
    navigationIconOnClick: () -> Unit,
    whenIsFocused: () -> Unit,
    whenNotHaveFocus: () -> Unit,
    titleOnChange: (String) -> Unit,
    descriptionOnChange: (String) -> Unit,
    onDeleteClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = editNoteUiState.isDoneIconVisible,
                doneIconOnClick = {
                    doneIconOnClick()
                    focusManager.clearFocus()
                },
                navigationIconOnClick = navigationIconOnClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TitleInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onFocusChanged { focusState ->
                        when {
                            focusState.isFocused -> whenIsFocused()
                            !focusState.hasFocus -> whenNotHaveFocus()
                        }
                    },
                title = editNoteUiState.title,
                titleOnChange = { title -> titleOnChange(title) }
            )
            ShowDate(date = editNoteUiState.createdAt)
            NoteContentView(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        when {
                            focusState.isFocused -> whenIsFocused()
                            !focusState.hasFocus -> whenNotHaveFocus()
                        }
                    },
                description = editNoteUiState.description,
                onDescriptionChange = { desc -> descriptionOnChange(desc) }
            )
            if (!editNoteUiState.isFocused) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = MaterialTheme.spacing.small)
                        .clickable {
                            onDeleteClicked()
                            navController.popBackStack()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_delete_24),
                        contentDescription = stringResource(id = R.string.delete_icon)
                    )
                    Text(
                        text = stringResource(id = R.string.delete),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowDate(
    date: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = date,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
        color = Color.Gray,
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
fun Preview() {
    /*EditNoteScreen(navController = rememberNavController())*/
}