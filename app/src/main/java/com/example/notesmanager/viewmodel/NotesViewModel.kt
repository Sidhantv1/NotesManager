package com.example.notesmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmanager.data.Note
import com.example.notesmanager.repository.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(private val repo: NotesRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val selectedTags = MutableStateFlow<List<String>>(emptyList())

    val notesFlow: StateFlow<List<Note>> =
        combine(
            repo.getNotes(),
            searchQuery,
            selectedTags
        ) { notes, query, tags ->
            var filtered = notes

            if (query.isNotBlank()) {
                val q = query.lowercase()
                filtered = filtered.filter {
                    it.title.lowercase().contains(q) ||
                            it.body.lowercase().contains(q) ||
                            it.tagsCsv.lowercase().contains(q)
                }
            }

            if (tags.isNotEmpty()) {
                filtered = filtered.filter { note ->
                    val noteTags = note.tagsCsv.split(",")
                    tags.all { selectedTag -> noteTags.contains(selectedTag) }
                }
            }
            filtered
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun setQuery(query: String) {
        searchQuery.value = query
    }

    fun setSelectedTags(tags: List<String>) {
        selectedTags.value = tags
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            repo.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }
}
