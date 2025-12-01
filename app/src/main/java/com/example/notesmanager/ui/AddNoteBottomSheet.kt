package com.example.notesmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.notesmanager.databinding.SheetAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class AddNoteBottomSheet(
    private val onAdd: (title: String, body: String, tags: List<String>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: SheetAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SheetAddNoteBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val body = binding.etBody.text.toString()

            val selectedTags = binding.chipGroupTags.children
                .filter { (it as Chip).isChecked }
                .map { (it as Chip).text.toString() }
                .toList()

            onAdd(title, body, selectedTags)
            dismiss()
        }

        return binding.root
    }
}
