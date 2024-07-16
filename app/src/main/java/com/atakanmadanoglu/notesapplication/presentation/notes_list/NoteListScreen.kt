package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import com.atakanmadanoglu.notesapplication.presentation.model.NotesListUIState
import com.atakanmadanoglu.notesapplication.presentation.model.NotesListUiOperation
import com.atakanmadanoglu.notesapplication.theme.dismissColor
import com.atakanmadanoglu.notesapplication.theme.openSansLightItalic
import com.atakanmadanoglu.notesapplication.theme.openSansRegular
import com.atakanmadanoglu.notesapplication.theme.openSansSemiBold
import com.atakanmadanoglu.notesapplication.theme.spacing
import com.atakanmadanoglu.notesapplication.theme.warningColor
import kotlinx.coroutines.delay

@Composable
internal fun NoteListRoute(
    addNoteButtonClicked: () -> Unit,
    cardOnClick: (Int) -> Unit,
    viewModel: NoteListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllNotes()
    }

    val showCheckbox by remember {
        derivedStateOf {
            uiState.operationType == NotesListUiOperation.SelectNotes
        }
    }

    val isAllSelected by remember {
        derivedStateOf {
            { uiState.selectedNotesCount == uiState.totalNotesCount }
        }
    }

    val isDeleteButtonEnabled by remember {
        derivedStateOf { { uiState.selectedNotesCount != 0 } }
    }

    NoteListScreen(
        notes = viewModel.getNotes(),
        notesListScreenState = uiState,
        showCheckbox = { showCheckbox },
        isAllSelected = isAllSelected,
        isDeleteButtonEnabled = isDeleteButtonEnabled,
        onEvent = {
            when (it) {
                NoteListEvent.AddNoteButtonClicked -> addNoteButtonClicked()
                NoteListEvent.CancelChoosingNote -> viewModel.onCancelButtonClicked()
                NoteListEvent.MakeSearchValueEmpty -> viewModel.setSearchValue("")
                NoteListEvent.OnDeleteClicked -> viewModel.onDeleteButtonClicked()
                NoteListEvent.OnDeleteOperationApproved -> viewModel.onDeletionApproved()
                NoteListEvent.OnDismissRequest -> viewModel.onDeletionDismissed()
                NoteListEvent.OnSelectAllClicked -> viewModel.onSelectAllClicked()
                is NoteListEvent.OnCardLongClick -> viewModel.onNoteCardLongPressed(it.noteIndex)
                is NoteListEvent.OnSearchValueChange -> viewModel.onSearchValueChange(it.newValue)
                is NoteListEvent.OnSwiped -> viewModel.deleteNote(it.index)
                is NoteListEvent.UpdateCheckboxState -> viewModel.onCheckboxClicked(it.index)
                is NoteListEvent.OnCardClick -> {
                    // If user presses long the note card before, cardOnClick will not be available
                    if (!showCheckbox) {
                        cardOnClick(it.noteId)
                    } else {
                        viewModel.onCheckboxClicked(it.noteIndex)
                    }
                }
            }
        }
    )
}

sealed class NoteListEvent {
    object AddNoteButtonClicked : NoteListEvent()
    object MakeSearchValueEmpty : NoteListEvent()
    data class OnCardClick(val noteId: Int, val noteIndex: Int) : NoteListEvent()
    data class OnCardLongClick(val noteIndex: Int) : NoteListEvent()
    data class OnSearchValueChange(val newValue: String) : NoteListEvent()
    object CancelChoosingNote : NoteListEvent()
    object OnDeleteClicked : NoteListEvent()
    object OnSelectAllClicked : NoteListEvent()
    object OnDismissRequest : NoteListEvent()
    object OnDeleteOperationApproved : NoteListEvent()
    data class UpdateCheckboxState(val index: Int) : NoteListEvent()
    data class OnSwiped(val index: Int) : NoteListEvent()
}


@Composable
fun NoteListScreen(
    notes: List<NoteUI>,
    notesListScreenState: NotesListUIState,
    showCheckbox: () -> Boolean,
    isAllSelected: () -> Boolean,
    isDeleteButtonEnabled: () -> Boolean,
    onEvent: (NoteListEvent) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = notesListScreenState.allNotesList) {
        onEvent(NoteListEvent.MakeSearchValueEmpty)
        listState.animateScrollToItem(0)
    }
    LaunchedEffect(key1 = notesListScreenState.searchedNotesList) {
        listState.animateScrollToItem(0)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        floatingActionButton = {
            // To make invisible fab during choosing any notes. This will
            // prevent users to add any note in that moment.
            when (notesListScreenState.operationType) {
                NotesListUiOperation.DisplayNotes -> Fab(fabClicked = { onEvent(NoteListEvent.AddNoteButtonClicked) })
                else -> {}
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            Column {
                when (notesListScreenState.operationType) {
                    NotesListUiOperation.DisplayNotes -> {
                        AllNotesText()
                        TotalNotesCount(notesCount = notesListScreenState.totalNotesCount)
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        SearchBar(
                            searchValue = notesListScreenState.searchValue,
                            onSearchValueChange = { onEvent(NoteListEvent.OnSearchValueChange(it)) }
                        )
                    }

                    NotesListUiOperation.SelectNotes -> {
                        Column {
                            CancelChoosingNoteButton(
                                cancelChoosingNote = { onEvent(NoteListEvent.CancelChoosingNote) }
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
                NoteListView(
                    notes = notes,
                    onCardClick = { noteId, noteIndex ->  onEvent(NoteListEvent.OnCardClick(noteId, noteIndex)) },
                    onCardLongClick = { onEvent(NoteListEvent.OnCardLongClick(it)) },
                    showCheckbox = showCheckbox,
                    updateCheckboxState = { onEvent(NoteListEvent.UpdateCheckboxState(it)) },
                    onSwiped = { onEvent(NoteListEvent.OnSwiped(it)) },
                    listState = listState
                )
            }
        }
        when (notesListScreenState.operationType) {
            NotesListUiOperation.SelectNotes ->  {
                OptionsWhenChosenNote(
                    isAllSelected = isAllSelected,
                    isDeleteButtonEnabled = isDeleteButtonEnabled,
                    onDeleteClicked = { onEvent(NoteListEvent.OnDeleteClicked) },
                    onSelectAllClicked = { onEvent(NoteListEvent.OnSelectAllClicked) }
                )
            }
            else -> {}
        }
        if (notesListScreenState.showDeletionDialog) {
            DeleteAlertDialog(
                selectedNoteCount = notesListScreenState.selectedNotesCount,
                onDismissRequest = { onEvent(NoteListEvent.OnDismissRequest) },
                onDeleteOperationApproved = { onEvent(NoteListEvent.OnDeleteOperationApproved) }
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
@OptIn(ExperimentalMaterial3Api::class)
fun SwipeBackground(dismissState: DismissState) {
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f, label = ""
    )
    Box(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Red)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "",
            modifier = Modifier.scale(scale)
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NoteListView(
    notes: List<NoteUI>,
    onCardClick: (noteId: Int, noteIndex: Int) -> Unit,
    onCardLongClick: (noteIndex: Int) -> Unit,
    showCheckbox: () -> Boolean,
    updateCheckboxState: (Int) -> Unit,
    onSwiped: (Int) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.extraExtraLarge
        ),
        state = listState
    ) {
        itemsIndexed(
            items = notes,
            key = { _, note ->  note.id }
        ) { index, note ->
            val swipeState = rememberDismissState()
            var isDeleted by remember { mutableStateOf(false) }
            if (swipeState.isDismissed(DismissDirection.EndToStart)) {
                LaunchedEffect(key1 = Unit) {
                    isDeleted = true
                    delay(300)
                    onSwiped(note.id)
                    isDeleted = false
                }
            }
            AnimatedVisibility(
                visible = !isDeleted,
                modifier = Modifier.animateItemPlacement()
            ) {
                SwipeToDismiss(
                    directions = setOf(DismissDirection.EndToStart),
                    state = swipeState,
                    background = { SwipeBackground(dismissState = swipeState) },
                    dismissContent = {
                        NoteRow(
                            onCardClick = { onCardClick(note.id, index) },
                            onCardLongClick = { onCardLongClick(index) },
                            showCheckbox = showCheckbox,
                            updateCheckboxState = { updateCheckboxState(index) },
                            title = note.title,
                            createdAt = note.createdAt,
                            description = note.description,
                            isChecked = note.isChecked
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
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
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onCardLongClick
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
        shape = RoundedCornerShape(12.dp)
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