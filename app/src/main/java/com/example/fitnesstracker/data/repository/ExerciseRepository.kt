package com.example.fitnesstracker.data.repository

import com.example.fitnesstracker.data.firebase.FirebaseService
import com.example.fitnesstracker.data.model.Exercise

class ExerciseRepository {
    private val firestore = FirebaseService.firestore
    private val exercisesCollection = firestore.collection("exercises")

    fun getExercises(onResult: (List<Exercise>) -> Unit) {
        exercisesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.toObjects(Exercise::class.java)
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
