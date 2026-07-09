package com.example.fitnesstracker.data.model

data class Workout(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val date: Long = System.currentTimeMillis()
)
