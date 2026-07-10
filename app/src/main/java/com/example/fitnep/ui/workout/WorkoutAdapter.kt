package com.example.fitnep.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnep.data.model.Workout
import com.example.fitnep.databinding.ItemWorkoutBinding
import java.text.SimpleDateFormat
import java.util.*

class WorkoutAdapter(
    private val onDeleteClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.tvWorkoutTitle.text = workout.title
            binding.tvWorkoutDuration.text = workout.duration
            binding.tvWorkoutDescription.text = workout.description
            binding.tvWorkoutDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(workout.date))
            binding.ivDeleteWorkout.setOnClickListener {
                onDeleteClick(workout)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean =
            oldItem == newItem
    }
}
