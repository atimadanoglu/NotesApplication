package com.atakanmadanoglu.notesapplication.presentation.notes_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.presentation.model.NoteUI
import com.atakanmadanoglu.notesapplication.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    addNoteButtonClicked: () -> Unit,
    cardOnClick: (Int) -> Unit,
    setDarkTheme: () -> Unit,
    iconId: Int,
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
        floatingActionButton = { Fab(addNoteButtonClicked) }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            AllNotesText(
                setDarkTheme = setDarkTheme,
                id = iconId
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            TotalNotesCount(notesCount = notesListScreenState.totalNotesCount)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SearchBar(searchValue = notesListScreenState.searchValue) { newValue ->
                viewModel.setSearchValue(newValue)
                viewModel.searchAndGetNotes()
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            NotesListView(
                notes = viewModel.decideWhichListWillBeUsed(),
                cardOnClick = cardOnClick
            )
        }
    }
}

@Composable
private fun AllNotesText(
    setDarkTheme: () -> Unit,
    id: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(id = R.string.all_notes),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = MaterialTheme.spacing.large)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = setDarkTheme,
                modifier = Modifier.padding(top = MaterialTheme.spacing.large)
            ) {
                Image(
                    painter = painterResource(id = id),
                    contentDescription = stringResource(id = R.string.theme_color_icon)
                )
            }
        }
    }

}

@Composable
private fun TotalNotesCount(
    notesCount: Int
) {
    val totalNotes = stringResource(id = R.string.total_notes_count, notesCount)
    Text(
        text = totalNotes,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String,
    onSearchValueChange: (newValue: String) -> Unit
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
        onValueChange = onSearchValueChange,
        singleLine = true,
        placeholder = { Text(
            text = stringResource(id = R.string.search_notes)
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
    cardOnClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(MaterialTheme.spacing.extraSmall)
    ) {
        items(notes) { note ->
            NoteRow(
                note = note,
                cardOnClick = cardOnClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteRow(
    modifier: Modifier = Modifier,
    cardOnClick: (Int) -> Unit,
    note: NoteUI
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = MaterialTheme.spacing.extraSmall),
        onClick = {
            cardOnClick(note.id)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = note.title,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.W400,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.createdAt,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1
                )
                Text(
                    text = stringResource(id = R.string.vertical_line),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
                Text(
                    text = note.description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    maxLines = 1
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
    NoteRow(note = NoteUI(1,"Hello", "There", "26 Oct"), cardOnClick = {})
}