package com.example.fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnesstracker.data.repository.AuthRepository
import com.example.fitnesstracker.utils.Resource

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginStatus = MutableLiveData<Resource<Boolean>>()
    val loginStatus: LiveData<Resource<Boolean>> = _loginStatus

    fun login(email: String, password: String) {
        _loginStatus.value = Resource.Loading()
        repository.login(email, password) { success, error ->
            if (success) {
                _loginStatus.value = Resource.Success(true)
            } else {
                _loginStatus.value = Resource.Error(error ?: "Login failed")
            }
        }
    }
}
