package com.example.fitnep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnep.data.model.User
import com.example.fitnep.data.repository.AuthRepository
import com.example.fitnep.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginStatus = MutableLiveData<Resource<User>>()
    val loginStatus: LiveData<Resource<User>> = _loginStatus

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginStatus.value = Resource.Error("Email and password cannot be empty")
            return
        }
        
        _loginStatus.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginStatus.value = result
        }
    }
}
