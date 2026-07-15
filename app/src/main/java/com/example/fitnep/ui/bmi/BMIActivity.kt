package com.example.fitnep.ui.bmi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnep.databinding.ActivityBmiBinding
import com.example.fitnep.utils.Resource
import com.example.fitnep.viewmodel.BMIViewModel

class BMIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiBinding
    private val viewModel: BMIViewModel by viewModels()
    private lateinit var adapter: BMIAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupUI()
        observeViewModel()
        
        viewModel.fetchBMIRecords()
    }

    private fun setupRecyclerView() {
        adapter = BMIAdapter()
        binding.rvBMIHistory.apply {
            layoutManager = LinearLayoutManager(this@BMIActivity)
            adapter = this@BMIActivity.adapter
        }
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCalculate.setOnClickListener {
            val weight = binding.etWeight.text.toString().toDoubleOrNull()
            val height = binding.etHeight.text.toString().toDoubleOrNull()

            if (weight != null && height != null && height > 0) {
                viewModel.saveBMI(weight, height)
                
                // Show local calculation immediately
                val bmi = weight / (height * height)
                updateResultCard(bmi)
            } else {
                Toast.makeText(this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateResultCard(bmi: Double) {
        binding.cvResult.visibility = android.view.View.VISIBLE
        binding.tvResult.text = String.format("BMI: %.2f", bmi)
        val status = getBMIStatus(bmi)
        binding.tvStatus.text = "Status: $status"
        
        val color = when (status) {
            "Underweight" -> 0xFF3B82F6.toInt() // Blue
            "Normal" -> 0xFF10B981.toInt()      // Green
            "Overweight" -> 0xFFF59E0B.toInt()   // Orange
            else -> 0xFFEF4444.toInt()           // Red
        }
        
        binding.tvResult.setTextColor(color)
        binding.cvResult.setCardBackgroundColor(android.content.res.ColorStateList.valueOf(adjustAlpha(color, 0.1f)))
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(android.graphics.Color.alpha(color) * factor)
        val red = android.graphics.Color.red(color)
        val green = android.graphics.Color.green(color)
        val blue = android.graphics.Color.blue(color)
        return android.graphics.Color.argb(alpha, red, green, blue)
    }

    private fun getBMIStatus(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
    }

    private fun observeViewModel() {
        viewModel.bmiRecords.observe(this) { resource ->
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

        viewModel.saveStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "BMI Saved", Toast.LENGTH_SHORT).show()
                    binding.etWeight.text?.clear()
                    binding.etHeight.text?.clear()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
