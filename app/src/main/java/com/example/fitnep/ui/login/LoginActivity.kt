package com.example.fitnep.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.fitnep.R
import com.example.fitnep.databinding.ActivityLoginBinding
import com.example.fitnep.ui.dashboard.DashboardActivity
import com.example.fitnep.ui.register.RegisterActivity
import com.example.fitnep.utils.Resource
import com.example.fitnep.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.loginStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.progressBar.isVisible = true
                    binding.btnLogin.text = ""
                }
                is Resource.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.progressBar.isVisible = false
                    binding.btnLogin.text = getString(R.string.login_button)
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    binding.progressBar.isVisible = false
                    binding.btnLogin.text = getString(R.string.login_button)
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
