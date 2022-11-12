package com.atakanmadanoglu.notesapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atakanmadanoglu.notesapplication.R
import com.atakanmadanoglu.notesapplication.model.Note
import com.atakanmadanoglu.notesapplication.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        floatingActionButton = { Fab() }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            AllNotesText(modifier)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            TotalNotesCount()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            SearchBar()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            val list = listOf(
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
                Note("abc","Hello", "there" ,"April 12, 12.58"),
            )
            NotesListView(notes = list)
        }
    }
}

@Composable
fun AllNotesText(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.all_notes),
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = MaterialTheme.spacing.large)
    )
}

@Composable
fun TotalNotesCount(
    notesCount: Int = 0
) {
    val totalNotesCount by remember {
        mutableStateOf(notesCount)
    }
    val totalNotes = stringResource(id = R.string.total_notes_count, totalNotesCount)
    Text(
        text = totalNotes,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String = ""
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = MaterialTheme.spacing.small)
    ) {
        var searchBarText by remember { mutableStateOf(searchValue) }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(5.dp, CircleShape),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = stringResource(id = R.string.search_icon)
                ) },
            value = searchBarText,
            onValueChange = { changedValue ->
                searchBarText = changedValue
            },
            singleLine = true,
            placeholder = { Text(text = stringResource(id = R.string.search_notes)) }
        )
    }
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar(modifier = Modifier)
}

@Composable
fun NotesListView(notes: List<Note>) {
    LazyColumn(
        contentPadding = PaddingValues(MaterialTheme.spacing.extraSmall)
    ) {
        items(notes) { note ->
            NoteRow(note = note)
        }
    }
}

@Composable
fun NoteRow(
    note: Note,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = MaterialTheme.spacing.extraSmall)
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
                    text = note.content,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun Fab() {
    FloatingActionButton(
        onClick = {},
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
    NoteRow(note = Note("abc","Hello", "There", "April 12, 12.34"))
}