package com.example.notesmanager.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesmanager.data.Note
import com.example.notesmanager.viewmodel.NotesViewModel
import java.nio.file.WatchEvent

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
                .background(Color(0xFFF1F1F2))
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Text(
                "Notes Manager",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Organise your notes with tags and search", modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 16.sp)
            )

            Spacer(Modifier.height(16.dp))

            SearchBar(viewModel)

            Spacer(Modifier.height(12.dp))

            Text(
                "Filter by tags:",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 16.sp)
            )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: NotesViewModel) {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = {
            query = it
            viewModel.setQuery(it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text("Search notes by title, body or tags...") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            Color.Black, Color.Black,
        )
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
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(16.dp)
        ) {

            // Title + Delete icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    note.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onDelete(note) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(note.body)

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                note.tagsCsv.split(",").forEach {
                    AssistChip(onClick = {}, label = { Text(it) })
                }
            }
        }

    }
}