package com.example.notesmanager.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notesmanager.R
import com.example.notesmanager.data.AppDatabase
import com.example.notesmanager.data.Note
import com.example.notesmanager.databinding.ActivityNotesBinding
import com.example.notesmanager.repository.NotesRepository
import com.example.notesmanager.viewmodel.NotesViewModel
import com.example.notesmanager.viewmodel.NotesViewModelFactory
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var adapter: NotesAdapter

    val tags = listOf("home", "personal", "prep", "shopping", "work")

    private val viewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(
            NotesRepository(
                Room.databaseBuilder(
                    this, AppDatabase::class.java, "notes_db"
                ).build().noteDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTags()
        setupRecycler()
        observeNotes()
        setupSearch()
        setupFab()
    }

    private fun setUpTags() {
        tags.forEach { tag ->
            val chip = Chip(this).apply {
                text = tag
                isCheckable = true
                isChecked = false
                isClickable = true
                setChipBackgroundColorResource(R.color.tag_background)
                setTextAppearance(R.style.TagTextStyle)

                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.setQuery(tag)
                    } else {
                        val anySelected = binding.chipGroupFilters.children
                            .any { (it as Chip).isChecked }

                        if (!anySelected) {
                            viewModel.setQuery("")
                        }
                    }
                }
            }
            binding.chipGroupFilters.addView(chip)
        }
    }

    private fun setupRecycler() {
        adapter = NotesAdapter(emptyList()) { note ->
            viewModel.deleteNote(note)
        }
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
    }

    private fun observeNotes() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notesFlow.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText.toString())
                return true
            }
        })
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            AddNoteBottomSheet { title, body, tags ->
                val note = Note(
                    title = title, body = body, tagsCsv = tags.joinToString(",")
                )
                viewModel.addNote(note)
            }.show(supportFragmentManager, "ADD_NOTE")
        }
    }
}