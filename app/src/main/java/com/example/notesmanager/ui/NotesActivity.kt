package com.example.notesmanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesmanager.R
import com.example.notesmanager.databinding.ActivityNotesBinding
import com.google.android.material.chip.Chip

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

    val tags = listOf("home", "personal", "prep", "shopping", "work")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tags.forEach { tag ->
            val chip = Chip(this).apply {
                text = tag
                isCheckable = true         // selectable
                isChecked = false
                isClickable = true
                setChipBackgroundColorResource(R.color.tag_background)
                setTextAppearance(R.style.TagTextStyle)
            }

            binding.chipGroupFilters.addView(chip)
        }
    }

}