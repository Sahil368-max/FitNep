package com.example.fitnesstracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivityLoginBinding
import com.example.fitnesstracker.ui.dashboard.DashboardActivity
import com.example.fitnesstracker.ui.register.RegisterActivity
import com.example.fitnesstracker.utils.Resource
import com.example.fitnesstracker.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.loginStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
