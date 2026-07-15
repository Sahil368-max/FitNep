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
            Exercise("Diamond Push Ups", "Chest", "Advanced", "10 min", android.R.drawable.ic_dialog_info, "Focuses on triceps and inner chest."),
            Exercise("Pistol Squats", "Legs", "Advanced", "12 min", android.R.drawable.ic_dialog_info, "One-legged squats for ultimate balance and strength."),
            Exercise("Muscle Ups", "Back", "Advanced", "15 min", android.R.drawable.ic_dialog_info, "Combines a pull-up and a dip into one powerful move."),
            Exercise("L-Sit", "Core", "Intermediate", "5 min", android.R.drawable.ic_dialog_info, "Static hold for core and arm strength."),
            Exercise("Bulgarian Split Squats", "Legs", "Intermediate", "15 min", android.R.drawable.ic_dialog_info, "Elevated rear leg squats for quad isolation."),
            Exercise("Incline Bench Press", "Chest", "Intermediate", "20 min", android.R.drawable.ic_dialog_info, "Targets the upper chest muscles."),
            Exercise("Sumo Deadlift", "Full Body", "Intermediate", "25 min", android.R.drawable.ic_dialog_info, "Wide stance deadlift for glute and inner thigh focus."),
            Exercise("Military Press", "Shoulders", "Intermediate", "15 min", android.R.drawable.ic_dialog_info, "Strict overhead press for shoulder boulders.")
        )

        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
            adapter = ExerciseAdapter(exercises)
        }
    }
}
