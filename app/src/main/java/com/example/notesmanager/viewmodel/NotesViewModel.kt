package com.example.notesmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmanager.data.Note
import com.example.notesmanager.repository.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(private val repo: NotesRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val notesFlow: StateFlow<List<Note>> =
        searchQuery
            .debounce(200)
            .flatMapLatest { query ->
                if (query.isBlank()) repo.getNotes()
                else repo.searchNotes(query)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )

    fun setQuery(query: String) {
        searchQuery.value = query
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
