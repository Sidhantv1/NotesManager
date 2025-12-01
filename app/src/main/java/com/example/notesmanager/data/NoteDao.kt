package com.example.notesmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getNotesFlow(): Flow<List<Note>>

    @Query(
        """
        SELECT * FROM notes
        WHERE title LIKE '%' || :query || '%' 
           OR body LIKE '%' || :query || '%'
           OR tagsCsv LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """
    )
    fun searchNotesFlow(query: String): Flow<List<Note>>
}
