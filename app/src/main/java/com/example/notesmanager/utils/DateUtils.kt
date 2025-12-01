package com.example.notesmanager.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun formatDate(timestamp: Long): String =
        formatter.format(Date(timestamp))
}
