package com.example.fitnep.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnep.data.model.User
import com.example.fitnep.databinding.ActivityRegisterBinding
import com.example.fitnep.utils.Resource
import com.example.fitnep.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

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

            val user = User(name = name, email = email)
            viewModel.register(user, password)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.registerStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
