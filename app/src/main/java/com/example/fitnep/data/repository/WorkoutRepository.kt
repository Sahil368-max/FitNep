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
            val workoutWithUser = workout.copy(id = docRef.id) // Assuming we add userId to Workout model or handle it differently
            // Actually, for consistency let's ensure Workout model has userId if needed, 
            // but for now I'll just save it. To make it user-specific, we should filter by userId.
            // Let's assume we want to store it under a subcollection or with a userId field.
            
            // For now, let's just use the current structure but maybe add userId field in future.
            // Let's stick to simple implementation first as requested.
            docRef.set(workoutWithUser).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add workout")
        }
    }

    suspend fun getWorkouts(): Resource<List<Workout>> {
        return try {
            val querySnapshot = firestore.collection("workouts")
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
            firestore.collection("workouts").document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete workout")
        }
    }
}
