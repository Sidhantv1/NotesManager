package com.example.notesmanager.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.notesmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteBottomSheetCompose(
    onAdd: (String, String, List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val tags = listOf("home", "personal", "prep", "shopping", "work")

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(emptyList<String>()) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(Modifier.padding(16.dp)) {

            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            TextField(
                value = body,
                onValueChange = { body = it },
                placeholder = { Text("Body") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tags) { tag ->
                    FilterChip(
                        selected = selectedTags.contains(tag),
                        onClick = {
                            selectedTags = if (selectedTags.contains(tag)) {
                                selectedTags - tag
                            } else {
                                selectedTags + tag
                            }
                        },
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_enter_a_title),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@Button
                    }
                    if (body.isBlank()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_enter_body),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@Button
                    }
                    if (selectedTags.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_select_at_least_one_tag),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@Button
                    }

                    onAdd(title, body, selectedTags)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Note")
            }
        }
    }
}
