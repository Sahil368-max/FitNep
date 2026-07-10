package com.example.fitnep.data.repository

import com.example.fitnep.data.model.User
import com.example.fitnep.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun register(user: User, password: String): Resource<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Registration failed")
            val newUser = user.copy(uid = uid)
            firestore.collection("users").document(uid).set(newUser).await()
            Resource.Success(newUser)
        } catch (e: Exception) {
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
