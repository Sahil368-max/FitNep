package com.example.fitnep.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitnep.data.model.User
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.databinding.ActivityProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val authRepository = AuthRepository()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadUserProfile()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun loadUserProfile() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.let { firebaseUser ->
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val userDoc = firestore.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .await()

                    val user = userDoc.toObject(User::class.java)
                    user?.let {
                        binding.etName.setText(it.name)
                        binding.etAge.setText(it.age.toString())
                        binding.etHeight.setText(it.height.toString())
                        binding.etWeight.setText(it.weight.toString())
                        binding.tvProfileName.text = it.name
                        binding.tvProfileEmail.text = it.email
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, "Error loading profile", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun updateProfile() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.let { firebaseUser ->
            val name = binding.etName.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0
            val height = binding.etHeight.text.toString().toDoubleOrNull() ?: 0.0
            val weight = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return
            }

            val userMap = mapOf(
                "name" to name,
                "age" to age,
                "height" to height,
                "weight" to weight
            )

            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .update(userMap)
                        .await()
                    
                    binding.tvProfileName.text = name
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}
