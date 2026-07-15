package com.example.fitnep.ui.exercises

data class Exercise(
    val name: String,
    val category: String,
    val difficulty: String, // Beginner, Intermediate, Advanced
    val duration: String,
    val iconRes: Int = android.R.drawable.ic_dialog_info,
    val description: String = ""
)
