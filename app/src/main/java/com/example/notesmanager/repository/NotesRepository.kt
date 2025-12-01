package com.example.notesmanager.repository

import com.example.notesmanager.data.Note
import com.example.notesmanager.data.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dao: NoteDao) {

    fun getNotes(): Flow<List<Note>> = dao.getNotesFlow()

    fun searchNotes(query: String): Flow<List<Note>> =
        dao.searchNotesFlow(query)

    suspend fun addNote(note: Note) =
        dao.insertNote(note)

    suspend fun deleteNote(note: Note) =
        dao.deleteNote(note)
}
