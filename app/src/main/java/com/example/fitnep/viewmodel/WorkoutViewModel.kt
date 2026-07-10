package com.example.fitnep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnep.data.model.Workout
import com.example.fitnep.data.repository.WorkoutRepository
import com.example.fitnep.utils.Resource
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {
    private val repository = WorkoutRepository()

    private val _workouts = MutableLiveData<Resource<List<Workout>>>()
    val workouts: LiveData<Resource<List<Workout>>> = _workouts

    private val _operationStatus = MutableLiveData<Resource<Unit>>()
    val operationStatus: LiveData<Resource<Unit>> = _operationStatus

    fun fetchWorkouts() {
        _workouts.value = Resource.Loading()
        viewModelScope.launch {
            _workouts.value = repository.getWorkouts()
        }
    }

    fun addWorkout(title: String, duration: String, description: String) {
        if (title.isBlank()) {
            _operationStatus.value = Resource.Error("Title cannot be empty")
            return
        }

        val workout = Workout(title = title, duration = duration, description = description)
        _operationStatus.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.addWorkout(workout)
            _operationStatus.value = result
            if (result is Resource.Success) {
                fetchWorkouts()
            }
        }
    }

    fun deleteWorkout(id: String) {
        _operationStatus.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.deleteWorkout(id)
            _operationStatus.value = result
            if (result is Resource.Success) {
                fetchWorkouts()
            }
        }
    }
}
