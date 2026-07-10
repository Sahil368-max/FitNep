package com.example.fitnep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnep.data.model.User
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.utils.Resource
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _registerStatus = MutableLiveData<Resource<User>>()
    val registerStatus: LiveData<Resource<User>> = _registerStatus

    fun register(user: User, password: String) {
        if (user.email.isBlank() || password.isBlank() || user.name.isBlank()) {
            _registerStatus.value = Resource.Error("Please fill in all fields")
            return
        }

        _registerStatus.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.register(user, password)
            _registerStatus.value = result
        }
    }
}
