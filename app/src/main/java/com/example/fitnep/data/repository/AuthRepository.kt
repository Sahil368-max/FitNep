package com.example.fitnep.data.repository

import android.util.Log
import com.example.fitnep.data.model.User
import com.example.fitnep.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "AuthRepository"

    suspend fun register(user: User, password: String): Resource<User> {
        return try {
            Log.d(TAG, "Attempting to create user: ${user.email}")
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Registration failed: No UID")
            
            Log.d(TAG, "Auth success, UID: $uid. Saving to Firestore...")
            val newUser = user.copy(uid = uid)
            firestore.collection("users").document(uid).set(newUser).await()
            
            Log.d(TAG, "Firestore save successful")
            Resource.Success(newUser)
        } catch (e: Exception) {
            Log.e(TAG, "Registration Error: ${e.message}", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Login failed")
            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java) ?: return Resource.Error("User not found")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}
