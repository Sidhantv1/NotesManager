package com.example.notesmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesmanager.data.Note
import com.example.notesmanager.viewmodel.NotesViewModel

@Composable
fun NotesAppScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notesFlow.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Notes Manager", style = MaterialTheme.typography.headlineSmall)

            Text("Organise your notes with tags and search")

            Spacer(Modifier.height(16.dp))

            SearchBar(viewModel)

            Spacer(Modifier.height(12.dp))

            TagFilters(viewModel)

            Spacer(Modifier.height(16.dp))

            NotesList(
                notes,
                onDelete = { viewModel.deleteNote(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }


    if (showSheet) {
        AddNoteBottomSheetCompose(
            onAdd = { title, body, tags ->
                viewModel.addNote(
                    Note(
                        title = title,
                        body = body,
                        tagsCsv = tags.joinToString(",")
                    )
                )
                showSheet = false
            },
            onDismiss = { showSheet = false }
        )
    }
}

@Composable
fun SearchBar(viewModel: NotesViewModel) {
    var query by remember { mutableStateOf("") }
    TextField(
        value = query,
        onValueChange = {
            query = it
            viewModel.setQuery(it)
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search notes") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun TagFilters(viewModel: NotesViewModel) {
    val tags = listOf("home", "personal", "prep", "shopping", "work")
    var selectedTags by remember { mutableStateOf(emptyList<String>()) }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tags) { tag ->
            FilterChip(
                selected = selectedTags.contains(tag),
                onClick = {
                    selectedTags = if (selectedTags.contains(tag)) {
                        selectedTags - tag
                    } else {
                        selectedTags + tag
                    }

                    viewModel.setSelectedTags(selectedTags)
                },
                label = { Text(tag) }
            )
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notes) { note ->
            NoteCard(note = note, onDelete = onDelete)
        }
    }
}


@Composable
fun NoteCard(note: Note, onDelete: (Note) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(note.body)

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                note.tagsCsv.split(",").forEach {
                    AssistChip(onClick = {}, label = { Text(it) })
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { onDelete(note) }) {
                Text("Delete")
            }
        }
    }
}