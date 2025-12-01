package com.example.notesmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import com.example.notesmanager.R
import com.example.notesmanager.databinding.SheetAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class AddNoteBottomSheet(
    private val onAdd: (title: String, body: String, tags: List<String>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: SheetAddNoteBinding

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = SheetAddNoteBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {

            val title = binding.etTitle.text.toString().trim()
            val body = binding.etBody.text.toString().trim()

            val selectedTags =
                binding.chipGroupTags.children.filter { it is Chip && (it as Chip).isChecked }
                    .map { (it as Chip).text.toString() }.toList()

            if (title.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_a_title), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (body.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_body), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (selectedTags.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_at_least_one_tag), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            onAdd(title, body, selectedTags)
            dismiss()
        }


        return binding.root
    }
}
