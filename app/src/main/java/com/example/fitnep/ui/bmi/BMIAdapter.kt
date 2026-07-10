package com.example.fitnep.ui.bmi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnep.data.model.BMI
import com.example.fitnep.databinding.ItemBmiBinding
import java.text.SimpleDateFormat
import java.util.*

class BMIAdapter : ListAdapter<BMI, BMIAdapter.BMIViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BMIViewHolder {
        val binding = ItemBmiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BMIViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BMIViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BMIViewHolder(private val binding: ItemBmiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bmi: BMI) {
            binding.tvBmiValue.text = "BMI: %.2f".format(bmi.bmiValue)
            binding.tvStatus.text = getBMIStatus(bmi.bmiValue)
            binding.tvDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(bmi.date))
        }

        private fun getBMIStatus(bmiValue: Double): String {
            return when {
                bmiValue < 18.5 -> "Underweight"
                bmiValue < 25.0 -> "Normal"
                bmiValue < 30.0 -> "Overweight"
                else -> "Obese"
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BMI>() {
        override fun areItemsTheSame(oldItem: BMI, newItem: BMI): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BMI, newItem: BMI): Boolean = oldItem == newItem
    }
}
