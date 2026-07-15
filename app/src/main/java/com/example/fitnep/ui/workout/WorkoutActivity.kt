package com.example.fitnep.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnep.databinding.ActivityWorkoutBinding
import com.example.fitnep.databinding.DialogAddWorkoutBinding
import com.example.fitnep.utils.Resource
import com.example.fitnep.viewmodel.WorkoutViewModel

class WorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding
    private val viewModel: WorkoutViewModel by viewModels()
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupUI()
        setupObservers()

        viewModel.fetchWorkouts()
    }

    private fun setupRecyclerView() {
        adapter = WorkoutAdapter(onDeleteClick = { workout ->
            viewModel.deleteWorkout(workout.id)
        })
        binding.rvWorkouts.apply {
            layoutManager = LinearLayoutManager(this@WorkoutActivity)
            adapter = this@WorkoutActivity.adapter
        }
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.fabAddWorkout.setOnClickListener {
            showAddWorkoutDialog()
        }
    }

    private fun setupObservers() {
        viewModel.workouts.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    adapter.submitList(resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.operationStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddWorkoutDialog() {
        val dialogBinding = DialogAddWorkoutBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Add Workout")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.etWorkoutTitle.text.toString()
                val duration = dialogBinding.etWorkoutDuration.text.toString()
                val description = dialogBinding.etWorkoutDescription.text.toString()
                viewModel.addWorkout(title, duration, description)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
