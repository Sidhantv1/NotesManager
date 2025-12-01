package com.example.notesmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesmanager.data.Note
import com.example.notesmanager.databinding.ItemNoteBinding
import com.example.notesmanager.utils.DateUtils
import com.google.android.material.chip.Chip

class NotesAdapter(
    private var notes: List<Note>,
    private val onDelete: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.tvTitle.text = note.title
        holder.binding.tvBody.text = note.body
        holder.binding.tvDate.text = DateUtils.formatDate(note.createdAt)

        // Tags
        holder.binding.chipGroupTags.removeAllViews()
        note.tagsCsv.split(",").forEach { tag ->
            val chip = Chip(holder.itemView.context).apply {
                text = tag.trim()
                isCheckable = false
                isClickable = false
            }
            holder.binding.chipGroupTags.addView(chip)
        }

        holder.binding.ivDelete.setOnClickListener {
            onDelete(note)
        }
    }

    override fun getItemCount() = notes.size

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
