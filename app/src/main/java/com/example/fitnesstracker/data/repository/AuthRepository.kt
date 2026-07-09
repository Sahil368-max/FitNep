package com.example.fitnesstracker.data.repository

import com.example.fitnesstracker.data.firebase.FirebaseService
import com.example.fitnesstracker.data.model.User
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val auth = FirebaseService.auth
    private val firestore = FirebaseService.firestore

    fun register(user: User, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    val userData = user.copy(uid = uid)
                    firestore.collection("users").document(uid).set(userData)
                        .addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, firestoreTask.exception?.message)
                            }
                        }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun getUserDetails(uid: String, onResult: (User?) -> Unit) {
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.toObject(User::class.java))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
