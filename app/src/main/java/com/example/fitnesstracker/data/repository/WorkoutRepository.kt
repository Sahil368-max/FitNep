package com.example.fitnesstracker.data.repository

import com.example.fitnesstracker.data.firebase.FirebaseService
import com.example.fitnesstracker.data.model.Workout

class WorkoutRepository {
    private val firestore = FirebaseService.firestore
    private val workoutsCollection = firestore.collection("workouts")

    fun addWorkout(workout: Workout, onResult: (Boolean, String?) -> Unit) {
        val id = workoutsCollection.document().id
        val newWorkout = workout.copy(id = id)
        workoutsCollection.document(id).set(newWorkout)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    fun getWorkouts(userId: String, onResult: (List<Workout>) -> Unit) {
        workoutsCollection.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.toObjects(Workout::class.java)
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateWorkout(workout: Workout, onResult: (Boolean, String?) -> Unit) {
        workoutsCollection.document(workout.id).set(workout)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    fun deleteWorkout(id: String, onResult: (Boolean, String?) -> Unit) {
        workoutsCollection.document(id).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }
}
