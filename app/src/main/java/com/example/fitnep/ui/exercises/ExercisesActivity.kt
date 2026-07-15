package com.example.fitnep.ui.exercises

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnep.databinding.ActivityExercisesBinding

class ExercisesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExercisesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExercisesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val exercises = listOf(
            Exercise("Push Ups", "Chest"),
            Exercise("Squats", "Legs"),
            Exercise("Pull Ups", "Back"),
            Exercise("Plank", "Core"),
            Exercise("Lunges", "Legs"),
            Exercise("Bench Press", "Chest"),
            Exercise("Deadlift", "Full Body"),
            Exercise("Shoulder Press", "Shoulders")
        )

        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
            adapter = ExerciseAdapter(exercises)
        }
    }
}
