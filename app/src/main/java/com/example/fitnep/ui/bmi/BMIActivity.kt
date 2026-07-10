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
        binding.btnCalculate.setOnClickListener {
            val weight = binding.etWeight.text.toString().toDoubleOrNull()
            val height = binding.etHeight.text.toString().toDoubleOrNull()

            if (weight != null && height != null && height > 0) {
                viewModel.saveBMI(weight, height)
            } else {
                Toast.makeText(this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.bmiRecords.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading
                }
                is Resource.Success -> {
                    adapter.submitList(resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.saveStatus.observe(this) { resource ->
            if (resource is Resource.Success) {
                Toast.makeText(this, "BMI Saved", Toast.LENGTH_SHORT).show()
                binding.etWeight.text?.clear()
                binding.etHeight.text?.clear()
            } else if (resource is Resource.Error) {
                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
