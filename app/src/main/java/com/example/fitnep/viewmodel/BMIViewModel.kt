package com.example.fitnep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnep.data.model.BMI
import com.example.fitnep.data.repository.BMIRepository
import com.example.fitnep.utils.Resource
import kotlinx.coroutines.launch

class BMIViewModel : ViewModel() {
    private val repository = BMIRepository()

    private val _bmiRecords = MutableLiveData<Resource<List<BMI>>>()
    val bmiRecords: LiveData<Resource<List<BMI>>> = _bmiRecords

    private val _saveStatus = MutableLiveData<Resource<Unit>>()
    val saveStatus: LiveData<Resource<Unit>> = _saveStatus

    fun fetchBMIRecords() {
        _bmiRecords.value = Resource.Loading()
        viewModelScope.launch {
            _bmiRecords.value = repository.getBMIRecords()
        }
    }

    fun saveBMI(weight: Double, height: Double) {
        val bmiValue = weight / (height * height)
        val bmi = BMI(weight = weight, height = height, bmiValue = bmiValue)
        
        _saveStatus.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.saveBMI(bmi)
            _saveStatus.value = result
            if (result is Resource.Success) {
                fetchBMIRecords()
            }
        }
    }
}
