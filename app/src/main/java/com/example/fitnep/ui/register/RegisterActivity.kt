package com.example.fitnep.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitnep.R
import com.example.fitnep.data.model.User
import com.example.fitnep.databinding.ActivityRegisterBinding
import com.example.fitnep.ui.dashboard.DashboardActivity
import com.example.fitnep.utils.Resource
import com.example.fitnep.viewmodel.RegisterViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val firestore = FirebaseFirestore.getInstance()
    private var currentUserUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(name = name, email = email)
            viewModel.register(user, password)
        }

        binding.btnCompleteSetup.setOnClickListener {
            completeProfileSetup()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun completeProfileSetup() {
        val age = binding.etAge.text.toString().toIntOrNull()
        val weight = binding.etWeight.text.toString().toDoubleOrNull()

        if (age == null || weight == null) {
            Toast.makeText(this, "Please enter valid age and weight", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = currentUserUid ?: return

        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnCompleteSetup.isEnabled = false
                
                firestore.collection("users").document(uid)
                    .update(mapOf(
                        "age" to age,
                        "weight" to weight
                    )).await()

                Toast.makeText(this@RegisterActivity, "Profile completed!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnCompleteSetup.isEnabled = true
            }
        }
    }

    private fun observeViewModel() {
        viewModel.registerStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnRegister.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    currentUserUid = resource.data?.uid
                    showProfileSetup()
                }
                is Resource.Error -> {
                    binding.btnRegister.isEnabled = true
                    binding.progressBar.visibility = android.view.View.GONE
                    val errorMessage = resource.message ?: getString(R.string.error_occurred)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showProfileSetup() {
        binding.llRegistrationFields.visibility = View.GONE
        binding.llProfileSetup.visibility = View.VISIBLE
    }
}
