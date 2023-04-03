package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.model.*
import com.atakanmadanoglu.notesapplication.theme.*
import java.util.*

@Composable
internal fun NotesListRoute(
    addNoteButtonClicked: () -> Unit,
    cardOnClick: (Int) -> Unit,
    viewModel: NotesListScreenViewModel = hiltViewModel()
) {
    val notesListScreenState by viewModel.state.collectAsStateWithLifecycle()

    NotesListScreen(
        addNoteButtonClicked = addNoteButtonClicked,
        cardOnClick = cardOnClick,
        notesListScreenState = notesListScreenState,
        makeSearchValueEmpty = { viewModel.setSearchValue("") },
        cardOnLongClick = { viewModel.onEvent(NotesListUiEvent.NoteCardLongPressed(it)) },
        cancelChoosingNoteOperation = { viewModel.onEvent(NotesListUiEvent.CancelButtonClicked) },
        getNotes = viewModel::decideWhichListWillBeUsed,
        updateCheckboxState = { viewModel.onEvent(NotesListUiEvent.CheckboxClicked(it)) },
        onDeleteClicked = { viewModel.onEvent(NotesListUiEvent.DeleteButtonClicked) },
        onSelectAllClicked = { viewModel.onEvent(NotesListUiEvent.SelectAllButtonClicked) },
        onDismissRequest = { viewModel.onEvent(NotesListUiEvent.DeletionDismissed) },
        onDeleteOperationApproved = { viewModel.onEvent(NotesListUiEvent.DeletionApproved) },
        onSearchValueChange = { viewModel.onEvent(NotesListUiEvent.SearchValueChanged(it)) },
        showCheckbox = {
            val showCheckbox = when (notesListScreenState.operationType) {
                NotesListUiOperation.SelectNotes -> true
                else -> false
            }
            showCheckbox
        }
    )
}

@Composable
fun NotesListScreen(
    addNoteButtonClicked: () -> Unit,
    makeSearchValueEmpty: () -> Unit,
    cardOnClick: (Int) -> Unit,
    cardOnLongClick: (noteIndex: Int) -> Unit,
    onSearchValueChange: (newValue: String) -> Unit,
    cancelChoosingNoteOperation: () -> Unit,
    getNotes: () -> List<NoteUI>,
    updateCheckboxState: (Int) -> Unit,
    onDeleteClicked: () -> Unit,
    onSelectAllClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onDeleteOperationApproved: () -> Unit,
    showCheckbox: () -> Boolean,
    notesListScreenState: NotesListUIState,
) {
    LaunchedEffect(key1 = notesListScreenState.allNotesList) {
        makeSearchValueEmpty()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        floatingActionButton = {
            // To make invisible fab during choosing any notes. This will
            // prevent users to add any note in that moment.
            when (notesListScreenState.operationType) {
                NotesListUiOperation.DisplayNotes -> Fab(addNoteButtonClicked)
                else -> {}
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            when (notesListScreenState.operationType) {
                NotesListUiOperation.DisplayNotes -> {
                    AllNotesText()
                    TotalNotesCount(notesCount = notesListScreenState.totalNotesCount)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    SearchBar(
                        searchValue = notesListScreenState.searchValue,
                        onSearchValueChange = onSearchValueChange
                    )
                }
                NotesListUiOperation.SelectNotes -> {
                    CancelChoosingNoteButton(
                        cancelChoosingNoteOperation = cancelChoosingNoteOperation
                    )
                    SelectedNumberOfNotesText(selectedNumberOfNotes = notesListScreenState.selectedNotesCount)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    SearchBar(
                        modifier = Modifier.alpha(0.2f),
                        searchValue = notesListScreenState.searchValue,
                        readOnly = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            NotesListView(
                notes = getNotes(),
                cardOnClick = cardOnClick,
                cardOnLongClick = cardOnLongClick,
                showCheckbox = showCheckbox,
                updateCheckboxState = updateCheckboxState
            )
        }
        when (notesListScreenState.operationType) {
            NotesListUiOperation.SelectNotes ->  {
                OptionsWhenChosenNote(
                    isAllSelected = { notesListScreenState.isAllSelected() },
                    isDeleteButtonEnabled = notesListScreenState.isDeleteButtonEnabled(),
                    onDeleteClicked = onDeleteClicked,
                    onSelectAllClicked = onSelectAllClicked
                )
            }
            else -> {}
        }
        when (notesListScreenState.operationType) {
            NotesListUiOperation.SelectNotes ->  {
                OptionsWhenChosenNote(
                    isAllSelected = { notesListScreenState.isAllSelected() },
                    isDeleteButtonEnabled = notesListScreenState.isDeleteButtonEnabled(),
                    onDeleteClicked = onDeleteClicked,
                    onSelectAllClicked = onSelectAllClicked
                )
            }
            else -> {}
        }
        if (notesListScreenState.showDeletionDialog) {
            DeleteAlertDialog(
                selectedNoteCount = notesListScreenState.selectedNotesCount,
                onDismissRequest = onDismissRequest,
                onDeleteOperationApproved = onDeleteOperationApproved
            )
        }
    }
}

@Composable
fun DeleteAlertDialog(
    selectedNoteCount: Int = 0,
    onDismissRequest: () -> Unit,
    onDeleteOperationApproved: () -> Unit
) {
    val questionText = if (selectedNoteCount != 1) {
        stringResource(id = R.string.delete_multiple_notes, selectedNoteCount)
    } else {
        stringResource(id = R.string.delete_one_note)
    }

    val dialogContainerColor = MaterialTheme.colorScheme.background

    val separatorColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surfaceVariant.copy(0.2f)
    } else {
        Color.Black.copy(0.1f)
    }

    val dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
    val maximumWidthFraction = 0.9f

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        val cancelText = stringResource(id = R.string.cancel)
        val separator = stringResource(id = R.string.vertical_line)
        val deleteText = stringResource(id = R.string.delete)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(maximumWidthFraction)
                    .wrapContentHeight()
                    .background(Color.Transparent),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = dialogContainerColor
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.medium,
                            bottom = MaterialTheme.spacing.small,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium
                        ),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = questionText,
                        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(maximumWidthFraction)
                            .padding(top = MaterialTheme.spacing.small),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = onDismissRequest,
                        ) {
                            Text(
                                text = cancelText.uppercase(),
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
                                color = dismissColor
                            )
                        }
                        Text(
                            text = separator,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
                            color = separatorColor,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        TextButton(
                            onClick = onDeleteOperationApproved
                        ) {
                            Text(
                                text = deleteText.uppercase(),
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
                                color = warningColor
                            )
                        }
                    }
                }
            }
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
    cardOnLongClick: (noteIndex: Int) -> Unit,
    showCheckbox: () -> Boolean,
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
        items(
            items = notes,
            key = { it.id }
        ) { note ->
            NoteRow(
                note = note,
                cardOnClick = cardOnClick,
                cardOnLongClick = {
                    cardOnLongClick(notes.indexOf(note))
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
    showCheckbox: () -> Boolean,
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
                    if (!showCheckbox()) {
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
            if (showCheckbox()) {
                Checkbox(
                    checked = note.isChecked,
                    onCheckedChange = { updateCheckboxState() }
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

@Composable
private fun OptionsWhenChosenNote(
    isDeleteButtonEnabled: Boolean,
    isAllSelected: () -> Boolean,
    onDeleteClicked: () -> Unit,
    onSelectAllClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        val deleteAlphaValue: Float =
            if (isDeleteButtonEnabled) 1f
            else 0.3f
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.small)
                    .weight(1f)
                    .clickable(enabled = isDeleteButtonEnabled) {
                        onDeleteClicked()
                    }
                    .alpha(deleteAlphaValue),
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

            Column(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.small)
                    .weight(1f)
                    .clickable { onSelectAllClicked() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val iconAndTextColor: Color
                val text: String

                if (isAllSelected()) {
                    iconAndTextColor = MaterialTheme.colorScheme.primary
                    text = stringResource(id = R.string.deselect_all)
                } else {
                    iconAndTextColor = MaterialTheme.colorScheme.onBackground
                    text = stringResource(id = R.string.select_all)
                }

                Icon(
                    painter = painterResource(id = R.drawable.select_all_56),
                    contentDescription = stringResource(id = R.string.select_all),
                    tint = iconAndTextColor
                )
                Text(
                    text = text,
                    color = iconAndTextColor,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontFamily = MaterialTheme.typography.openSansRegular.fontFamily
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    /*NoteRow(note = NoteUI(1,"Hello", "There", "26 Oct"), cardOnClick = {},)*/
}