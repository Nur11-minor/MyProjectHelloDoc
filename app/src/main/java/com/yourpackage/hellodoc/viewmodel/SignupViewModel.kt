package com.yourpackage.hellodoc.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.hellodoc.models.User
import com.yourpackage.hellodoc.repository.AuthRepository
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> = _signupState

    fun signup(user: User, password: String) {
        viewModelScope.launch {
            Log.d("SignupViewModel", "Emitting Loading state")
            _signupState.value = SignupState.Loading
            Log.d("SignupViewModel", "Calling repository.signup...")
            val result = repository.signup(user, password)
            Log.d("SignupViewModel", "Repository returned result: ${result.isSuccess}")
            result.onSuccess { registeredUser ->
                Log.d("SignupViewModel", "Emitting Success state")
                _signupState.value = SignupState.Success(registeredUser)
            }.onFailure { exception ->
                Log.e("SignupViewModel", "Emitting Error state: ${exception.message}")
                _signupState.value = SignupState.Error(exception.message ?: "Signup failed")
            }
        }
    }

    sealed class SignupState {
        object Loading : SignupState()
        data class Success(val user: User) : SignupState()
        data class Error(val message: String) : SignupState()
    }
}
