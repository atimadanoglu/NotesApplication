package com.atakanmadanoglu.notesapplication.presentation.edit_note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.atakanmadanoglu.notesapplication.presentation.model.EditNoteAction
import com.atakanmadanoglu.notesapplication.presentation.model.EditNoteUiState
import com.atakanmadanoglu.notesapplication.presentation.notes_list.DeleteAlertDialog
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.spacing

@Composable
internal fun EditNoteRoute(
    id: Int,
    navController: NavController,
    viewModel: EditNoteScreenViewModel = hiltViewModel()
) {
    val editNoteUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(id) {
        viewModel.getNoteById(id)
    }

    EditNoteScreen(
        uiState = editNoteUiState,
        onAction = { action ->
            when (action) {
                EditNoteAction.OnBackClicked -> { navController.navigateUp() }
                EditNoteAction.OnDeleteClicked -> { viewModel.onDeleteButtonClicked() }
                is EditNoteAction.OnDescriptionChanged -> { viewModel.onDescriptionChanged(action.desc) }
                is EditNoteAction.OnTitleChanged -> { viewModel.onTitleChanged(action.title) }
                is EditNoteAction.OnFocusChanged -> { viewModel.setFocus(action.isFocused) }
                EditNoteAction.OnDismissRequest -> { viewModel.onDeletionDismissed() }
                EditNoteAction.OnDeleteApproved -> {
                    viewModel.deleteNote(id)
                    navController.navigateUp()
                }
                EditNoteAction.OnDoneIconClicked -> {
                    viewModel.onDoneIconClicked()
                    focusManager.clearFocus()
                }
            }
        }
    )
}

@Composable
fun EditNoteScreen(
    uiState: EditNoteUiState,
    onAction: (EditNoteAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
        topBar = {
            NavigationTopAppBar(
                isDoneIconVisible = uiState.isDoneIconVisible,
                onDoneIconClick = { onAction(EditNoteAction.OnDoneIconClicked) },
                onNavigationIconClick = { onAction(EditNoteAction.OnBackClicked) }
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
                            focusState.isFocused -> { onAction(EditNoteAction.OnFocusChanged(true)) }
                            !focusState.hasFocus -> { onAction(EditNoteAction.OnFocusChanged(false)) }
                        }
                    },
                title = uiState.title,
                onTitleChange = { title -> onAction(EditNoteAction.OnTitleChanged(title)) },
            )
            ShowDate(date = uiState.createdAt)
            NoteContentView(
                description = uiState.description,
                onDescriptionChange = { desc -> onAction(EditNoteAction.OnDescriptionChanged(desc)) },
            )
            if (!uiState.isFocused) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = MaterialTheme.spacing.small)
                        .clickable { onAction(EditNoteAction.OnDeleteClicked) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DeleteButton()
                }
            }
        }
        if (uiState.openDeleteDialog) {
            DeleteAlertDialog(
                selectedNoteCount = 1, // default value is 1
                onDismissRequest = { onAction(EditNoteAction.OnDismissRequest) },
                onDeleteOperationApproved = { onAction(EditNoteAction.OnDeleteApproved) }
            )
        }
    }
}

@Composable
private fun DeleteButton() {
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

@Composable
private fun ShowDate(date: String) {
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
    EditNoteScreen(EditNoteUiState(), onAction = {})
}