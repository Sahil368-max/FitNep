package com.example.fitnep.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.databinding.ActivityDashboardBinding
import com.example.fitnep.ui.bmi.BMIActivity
import com.example.fitnep.ui.login.LoginActivity
import com.example.fitnep.ui.workout.WorkoutActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.cvWorkout.setOnClickListener {
            startActivity(Intent(this, WorkoutActivity::class.java))
        }

        binding.cvBMI.setOnClickListener {
            startActivity(Intent(this, BMIActivity::class.java))
        }

        // Other feature cards can be implemented later
    }
}
