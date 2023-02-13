package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import com.atakanmadanoglu.notesapplication.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    addNoteButtonClicked: () -> Unit,
    cardOnClick: (Int) -> Unit,
    viewModel: NotesListScreenViewModel = hiltViewModel()
) {
    val notesListScreenState by remember {
        mutableStateOf(viewModel.notesListUiState)
    }
    viewModel.getAllNotes()
    // To display all list after editing or adding a note
    viewModel.setSearchValue("")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        floatingActionButton = {
            // To make invisible fab during choosing any notes. This will
            // prevent users to add any note in that moment.
            if (!notesListScreenState.startChoosingNoteOperation) {
                Fab(addNoteButtonClicked)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            if (!notesListScreenState.startChoosingNoteOperation) {
                AllNotesText()
                TotalNotesCount(notesCount = notesListScreenState.totalNotesCount)
            } else {
                CancelChoosingNoteButton(
                    cancelChoosingNoteOperation = {
                        viewModel.setStartChoosingNoteOperation(false)
                        viewModel.setAllNotesCheckboxUnchecked()
                    }
                )
                SelectedNumberOfNotesText(selectedNumberOfNotes = viewModel.getSelectedNotesCount())
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            if (!notesListScreenState.startChoosingNoteOperation) {
                SearchBar(
                    searchValue = notesListScreenState.searchValue
                ) { newValue ->
                    viewModel.setSearchValue(newValue)
                    viewModel.searchAndGetNotes()
                }
            } else {
                SearchBar(
                    modifier = Modifier.alpha(0.2f),
                    searchValue = notesListScreenState.searchValue,
                    readOnly = true
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            NotesListView(
                notes = viewModel.decideWhichListWillBeUsed(),
                cardOnClick = cardOnClick,
                cardOnLongClick = { noteId, noteIndex ->
                    viewModel.setStartChoosingNoteOperation(true)
                    viewModel.setNoteCheckedState(noteIndex)
                },
                showCheckbox = notesListScreenState.startChoosingNoteOperation,
                updateCheckboxState = { noteIndex ->
                    viewModel.setNoteCheckedState(noteIndex)
                }
            )
        }
    }
}

@Composable
private fun CancelChoosingNoteButton(
    cancelChoosingNoteOperation: () -> Unit
) {
    IconButton(
        modifier = Modifier.padding(
            top = MaterialTheme.spacing.medium
        ),
        onClick = cancelChoosingNoteOperation
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_close_48),
            contentDescription = stringResource(id = R.string.close_icon)
        )
    }
}

@Composable
private fun SelectedNumberOfNotesText(
    modifier: Modifier = Modifier,
    selectedNumberOfNotes: Int
) {
    val text = when {
        selectedNumberOfNotes == 1 -> {
            stringResource(id = R.string.singular_selected_number_of_notes, selectedNumberOfNotes)
        }
        selectedNumberOfNotes > 1 -> {
            stringResource(id = R.string.plural_selected_number_of_notes, selectedNumberOfNotes)
        }
        else -> stringResource(id = R.string.none_item_selected)
    }

    Text(
        text = text,
        fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            top = MaterialTheme.spacing.medium,
            start = MaterialTheme.spacing.small
        )
    )
}

@Composable
private fun AllNotesText(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.all_notes),
        fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily,
        fontSize = MaterialTheme.typography.displayMedium.fontSize,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            top = MaterialTheme.spacing.large,
            start = MaterialTheme.spacing.small
        )
    )
}

@Composable
private fun TotalNotesCount(
    modifier: Modifier = Modifier,
    notesCount: Int
) {
    val totalNotes = if (notesCount != 1) {
        stringResource(id = R.string.plural_note_count, notesCount)
    } else {
        stringResource(id = R.string.singular_note_count, notesCount)
    }
    Text(
        modifier = modifier.padding(start = MaterialTheme.spacing.small),
        text = totalNotes,
        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
        fontSize = MaterialTheme.typography.titleMedium.fontSize
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String,
    readOnly: Boolean = false,
    onSearchValueChange: (newValue: String) -> Unit = {}
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(48.dp)),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                contentDescription = stringResource(id = R.string.search_icon)
            ) },
        value = searchValue,
        readOnly = readOnly,
        textStyle = TextStyle(fontFamily = MaterialTheme.typography.openSansRegular.fontFamily),
        onValueChange = onSearchValueChange,
        singleLine = true,
        placeholder = { Text(
            text = stringResource(id = R.string.search_notes),
            fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
        ) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun NotesListView(
    notes: List<NoteUI>,
    cardOnClick: (Int) -> Unit,
    cardOnLongClick: (noteId: Int, noteIndex: Int) -> Unit,
    showCheckbox: Boolean,
    updateCheckboxState: (noteIndex: Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = MaterialTheme.spacing.extraSmall,
            top = MaterialTheme.spacing.extraSmall,
            end = MaterialTheme.spacing.extraSmall,
            bottom = MaterialTheme.spacing.extraExtraLarge
        )
    ) {
        items(notes) { note ->
            NoteRow(
                note = note,
                cardOnClick = cardOnClick,
                cardOnLongClick = {
                    cardOnLongClick(note.id, notes.indexOf(note))
                },
                showCheckbox = showCheckbox,
                updateCheckboxState = {
                    updateCheckboxState(notes.indexOf(note))
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteRow(
    modifier: Modifier = Modifier,
    cardOnClick: (Int) -> Unit,
    cardOnLongClick: () -> Unit,
    updateCheckboxState: () -> Unit,
    showCheckbox: Boolean,
    note: NoteUI
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = MaterialTheme.spacing.extraSmall)
            .combinedClickable(
                onClick = {
                    // If user presses long the note card before, cardOnClick will not be available
                    if (!showCheckbox) {
                        cardOnClick(note.id)
                    } else {
                        updateCheckboxState()
                    }
                },
                onLongClick = {
                    cardOnLongClick()
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
                    .padding(
                        start = MaterialTheme.spacing.medium
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = note.title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                Row {
                    Text(
                        text = note.createdAt,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = MaterialTheme.typography.openSansLightItalic.fontFamily,
                        maxLines = 1
                    )
                    Text(
                        text = stringResource(id = R.string.vertical_line),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily
                    )
                    Text(
                        text = note.description,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
                        maxLines = 1
                    )

                }
            }
            if (showCheckbox) {
                Checkbox(
                    checked = note.isChecked.value,
                    onCheckedChange = {
                        note.isChecked.value = it
                    }
                )
            }
        }
    }
}

@Composable
fun Fab(
    fabClicked: () -> Unit
) {
    FloatingActionButton(
        onClick = fabClicked,
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = stringResource(id = R.string.add_note),
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun Preview() {
    /*NoteRow(note = NoteUI(1,"Hello", "There", "26 Oct"), cardOnClick = {},)*/
}