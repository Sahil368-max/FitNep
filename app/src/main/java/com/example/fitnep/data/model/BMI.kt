package com.example.fitnep.data.model

data class BMI(
    val id: String = "",
    val userId: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val bmiValue: Double = 0.0,
    val date: Long = System.currentTimeMillis()
)
