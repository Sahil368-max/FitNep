package com.example.fitnep.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitnep.data.model.User
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.databinding.ActivityDashboardBinding
import com.example.fitnep.ui.bmi.BMIActivity
import com.example.fitnep.ui.exercises.ExercisesActivity
import com.example.fitnep.ui.login.LoginActivity
import com.example.fitnep.ui.profile.ProfileActivity
import com.example.fitnep.ui.workout.WorkoutActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val authRepository = AuthRepository()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.let { firebaseUser ->
            lifecycleScope.launch {
                try {
                    val userDoc = firestore.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .await()
                    
                    val user = userDoc.toObject(User::class.java)
                    user?.let {
                        binding.tvUserName.text = it.name
                    }
                } catch (e: Exception) {
                    // Fallback to email if name fetch fails
                    binding.tvUserName.text = firebaseUser.email?.split("@")?.get(0) ?: "Fitness Enthusiast"
                }
            }
        }
    }

    private fun setupUI() {
        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.cvWorkout.setOnClickListener {
            startActivity(Intent(this, WorkoutActivity::class.java))
        }

        binding.cvBMI.setOnClickListener {
            startActivity(Intent(this, BMIActivity::class.java))
        }
        
        binding.cvExercises.setOnClickListener {
            startActivity(Intent(this, ExercisesActivity::class.java))
        }
        
        binding.cvProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this, WorkoutActivity::class.java))
        }
    }
}
