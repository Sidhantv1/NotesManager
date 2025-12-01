package com.example.notesmanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesmanager.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}