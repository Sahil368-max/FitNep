package com.example.fitnesstracker.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.data.model.User
import com.example.fitnesstracker.databinding.ActivityRegisterBinding
import com.example.fitnesstracker.utils.Resource
import com.example.fitnesstracker.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(name = name, email = email)
                viewModel.register(user, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }

        viewModel.registerStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
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
