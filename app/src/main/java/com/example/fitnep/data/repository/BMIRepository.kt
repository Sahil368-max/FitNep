package com.example.fitnep.data.repository

import com.example.fitnep.data.model.BMI
import com.example.fitnep.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class BMIRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveBMI(bmi: BMI): Resource<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("User not logged in")
            val docRef = firestore.collection("bmi_records").document()
            val bmiWithId = bmi.copy(id = docRef.id, userId = userId)
            docRef.set(bmiWithId).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to save BMI")
        }
    }

    suspend fun getBMIRecords(): Resource<List<BMI>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Resource.Error("User not logged in")
            val querySnapshot = firestore.collection("bmi_records")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get().await()
            val records = querySnapshot.toObjects(BMI::class.java)
            Resource.Success(records)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch records")
        }
    }
}
