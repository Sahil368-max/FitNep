package com.example.fitnep.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.ui.dashboard.DashboardActivity
import com.example.fitnep.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (authRepository.getCurrentUser() != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
