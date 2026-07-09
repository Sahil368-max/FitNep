package com.example.fitnesstracker.data.model

data class BMI(
    val id: String = "",
    val userId: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val bmiValue: Double = 0.0,
    val status: String = "",
    val date: Long = System.currentTimeMillis()
)
