package com.example.fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnesstracker.data.model.User
import com.example.fitnesstracker.data.repository.AuthRepository
import com.example.fitnesstracker.utils.Resource

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _registerStatus = MutableLiveData<Resource<Boolean>>()
    val registerStatus: LiveData<Resource<Boolean>> = _registerStatus

    fun register(user: User, password: String) {
        _registerStatus.value = Resource.Loading()
        repository.register(user, password) { success, error ->
            if (success) {
                _registerStatus.value = Resource.Success(true)
            } else {
                _registerStatus.value = Resource.Error(error ?: "Registration failed")
            }
        }
    }
}
