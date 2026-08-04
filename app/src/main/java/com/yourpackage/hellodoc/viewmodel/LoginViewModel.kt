package com.yourpackage.hellodoc.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.hellodoc.models.User
import com.yourpackage.hellodoc.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "Emitting Loading state")
            _loginState.value = LoginState.Loading
            Log.d("LoginViewModel", "Calling repository.login...")
            val result = repository.login(email, password)
            Log.d("LoginViewModel", "Repository returned result: ${result.isSuccess}")
            result.onSuccess { user ->
                Log.d("LoginViewModel", "Emitting Success state")
                _loginState.value = LoginState.Success(user)
            }.onFailure { exception ->
                Log.e("LoginViewModel", "Emitting Error state: ${exception.message}")
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        }
    }

    sealed class LoginState {
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
