package com.example.notesmanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.notesmanager.repository.NotesRepository
import com.example.notesmanager.viewmodel.NotesViewModel
import com.example.notesmanager.viewmodel.NotesViewModelFactory

class NotesComposeActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(
            NotesRepository(
                AppDatabase.getInstance(applicationContext).noteDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppScreen(viewModel)
        }
    }
}