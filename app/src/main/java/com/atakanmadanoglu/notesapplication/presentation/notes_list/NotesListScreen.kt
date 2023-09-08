package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

    val cancelChoosingNote: () -> Unit = remember {
        return@remember viewModel::onCancelButtonClicked
    }

    val showCheckbox by remember {
        derivedStateOf {
            when (notesListScreenState.operationType) {
                NotesListUiOperation.SelectNotes -> true
                else -> false
            }
        }
    }

    val isAllSelected by remember {
        derivedStateOf {
            { notesListScreenState.selectedNotesCount == notesListScreenState.totalNotesCount }
        }
    }

    val isDeleteButtonEnabled by remember {
        derivedStateOf {
            { notesListScreenState.selectedNotesCount != 0 }
        }
    }
    NotesListScreen(
        addNoteButtonClicked = addNoteButtonClicked,
        onCardClick = { noteId: Int, noteIndex: Int ->
            // If user presses long the note card before, cardOnClick will not be available
            if (!showCheckbox) {
                cardOnClick(noteId)
            } else {
                viewModel.onCheckboxClicked(noteIndex)
            }
        },
        notesListScreenState = notesListScreenState,
        makeSearchValueEmpty = { viewModel.setSearchValue("") },
        onCardLongClick = { viewModel.onNoteCardLongPressed(it) },
        cancelChoosingNote = cancelChoosingNote,
        getNotes = viewModel::decideWhichListWillBeUsed,
        onDeleteClicked = viewModel::onDeleteButtonClicked,
        onSelectAllClicked = viewModel::onSelectAllClicked,
        onDismissRequest = viewModel::onDeletionDismissed,
        onDeleteOperationApproved = viewModel::onDeletionApproved,
        onSearchValueChange = { viewModel.onSearchValueChange(it) },
        showCheckbox = {
            showCheckbox
        },
        updateCheckboxState = {
            viewModel.onCheckboxClicked(it)
        },
        isAllSelected = isAllSelected,
        isDeleteButtonEnabled = isDeleteButtonEnabled
    )
}

@Composable
fun NotesListScreen(
    addNoteButtonClicked: () -> Unit,
    makeSearchValueEmpty: () -> Unit,
    onCardClick: (noteId: Int, noteIndex: Int) -> Unit,
    onCardLongClick: (noteIndex: Int) -> Unit,
    onSearchValueChange: (newValue: String) -> Unit,
    cancelChoosingNote: () -> Unit,
    getNotes: () -> List<NoteUI>,
    onDeleteClicked: () -> Unit,
    onSelectAllClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onDeleteOperationApproved: () -> Unit,
    notesListScreenState: NotesListUIState,
    showCheckbox: () -> Boolean,
    updateCheckboxState: (Int) -> Unit,
    isAllSelected: () -> Boolean,
    isDeleteButtonEnabled: () -> Boolean
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
                    Column {
                        CancelChoosingNoteButton(
                            cancelChoosingNote = cancelChoosingNote
                        )
                    }
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
                onCardClick = onCardClick,
                onCardLongClick = onCardLongClick,
                showCheckbox = showCheckbox,
                updateCheckboxState = updateCheckboxState
            )
        }
        when (notesListScreenState.operationType) {
            NotesListUiOperation.SelectNotes ->  {
                OptionsWhenChosenNote(
                    isAllSelected = isAllSelected,
                    isDeleteButtonEnabled = isDeleteButtonEnabled,
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
    cancelChoosingNote: () -> Unit
) {
    IconButton(
        modifier = Modifier.padding(
            top = MaterialTheme.spacing.medium
        ),
        onClick = cancelChoosingNote
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
    onCardClick: (noteId: Int, noteIndex: Int) -> Unit,
    onCardLongClick: (noteIndex: Int) -> Unit,
    showCheckbox: () -> Boolean,
    updateCheckboxState: (Int) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = MaterialTheme.spacing.extraSmall,
            top = MaterialTheme.spacing.extraSmall,
            end = MaterialTheme.spacing.extraSmall,
            bottom = MaterialTheme.spacing.extraExtraLarge
        ),
        state = listState
    ) {
        items(
            items = notes,
            key = { it.id }
        ) { note ->
            val index = notes.indexOf(note)
            NoteRow(
                onCardClick = { onCardClick(note.id, index) },
                onCardLongClick = {
                    onCardLongClick(index)
                },
                showCheckbox = showCheckbox,
                updateCheckboxState = {
                    updateCheckboxState(index)
                },
                title = note.title,
                createdAt = note.createdAt,
                description = note.description,
                isChecked = note.isChecked
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteRow(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onCardLongClick: () -> Unit,
    showCheckbox: () -> Boolean,
    updateCheckboxState: () -> Unit,
    title: String,
    createdAt: String,
    description: String,
    isChecked: Boolean
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = MaterialTheme.spacing.extraSmall)
            .combinedClickable(
                onClick = onCardClick/* {
                    // If user presses long the note card before, cardOnClick will not be available
                    if (!showCheckbox()) {
                        onCardClick(note.id)
                    } else {
                        updateCheckboxState()
                    }
                }*/,
                onLongClick = onCardLongClick
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
                    text = title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = MaterialTheme.typography.openSansSemiBold.fontFamily,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                Row {
                    Text(
                        text = createdAt,
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
                        text = description,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = MaterialTheme.typography.openSansRegular.fontFamily,
                        maxLines = 1
                    )

                }
            }
            if (showCheckbox()) {
                Checkbox(
                    checked = isChecked,
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
    isDeleteButtonEnabled: () -> Boolean,
    isAllSelected: () -> Boolean,
    onDeleteClicked: () -> Unit,
    onSelectAllClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        val deleteAlphaValue: Float =
            if (isDeleteButtonEnabled()) 1f
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
                    .clickable(enabled = isDeleteButtonEnabled()) {
                        onDeleteClicked()
                    }
                    .alpha(deleteAlphaValue),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DeleteOption()
            }

            Column(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.small)
                    .weight(1f)
                    .clickable { onSelectAllClicked() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SelectAllOption(
                    isAllSelected = isAllSelected()
                )
            }
        }
    }
}

@Composable
private fun DeleteOption() {
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
private fun SelectAllOption(
    isAllSelected: Boolean
) {
    val iconAndTextColor: Color
    val text: String

    if (isAllSelected) {
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

@Preview
@Composable
fun Preview() {
    NotesListScreen(
        addNoteButtonClicked = { /*TODO*/ },
        makeSearchValueEmpty = { /*TODO*/ },
        onCardClick = {noteId, noteIndex ->  } ,
        onCardLongClick = {},
        onSearchValueChange = {},
        cancelChoosingNote = { },
        getNotes = { listOf() },
        updateCheckboxState = {},
        onDeleteClicked = {  },
        onSelectAllClicked = {  },
        onDismissRequest = { },
        onDeleteOperationApproved = { },
        showCheckbox = { true },
        notesListScreenState = NotesListUIState(),
        isAllSelected = {false},
        isDeleteButtonEnabled = {false}
    )
}