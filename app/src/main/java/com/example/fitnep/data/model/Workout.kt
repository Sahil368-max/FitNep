package com.example.fitnep.data.model

data class Workout(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val date: Long = System.currentTimeMillis()
)
