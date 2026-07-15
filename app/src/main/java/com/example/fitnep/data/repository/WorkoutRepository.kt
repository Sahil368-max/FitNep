package com.example.fitnep.data.repository

import com.example.fitnep.data.model.Workout
import com.example.fitnep.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class WorkoutRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addWorkout(workout: Workout): Resource<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("User not logged in")
            val docRef = firestore.collection("workouts").document()
            val workoutWithUser = workout.copy(id = docRef.id, userId = userId)
            docRef.set(workoutWithUser).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add workout")
        }
    }

    suspend fun getWorkouts(): Resource<List<Workout>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("User not logged in")
            val querySnapshot = firestore.collection("workouts")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get().await()
            val list = querySnapshot.toObjects(Workout::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch workouts")
        }
    }

    suspend fun deleteWorkout(id: String): Resource<Unit> {
        return try {
            if (auth.currentUser == null) return Resource.Error("User not logged in")
            firestore.collection("workouts").document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete workout")
        }
    }
}
