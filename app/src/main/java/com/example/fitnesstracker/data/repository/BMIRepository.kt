package com.example.fitnesstracker.data.repository

import com.example.fitnesstracker.data.firebase.FirebaseService
import com.example.fitnesstracker.data.model.BMI

class BMIRepository {
    private val firestore = FirebaseService.firestore
    private val bmiCollection = firestore.collection("bmi")

    fun saveBMI(bmi: BMI, onResult: (Boolean, String?) -> Unit) {
        val id = bmiCollection.document().id
        val newBMI = bmi.copy(id = id)
        bmiCollection.document(id).set(newBMI)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    fun getBMIHistory(userId: String, onResult: (List<BMI>) -> Unit) {
        bmiCollection.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.toObjects(BMI::class.java)
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
