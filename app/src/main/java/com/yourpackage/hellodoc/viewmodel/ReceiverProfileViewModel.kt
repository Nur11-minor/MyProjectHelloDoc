package com.yourpackage.hellodoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.hellodoc.models.User
import com.yourpackage.hellodoc.repository.AuthRepository
import kotlinx.coroutines.launch

class ReceiverProfileViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    fun loadProfile() {
        viewModelScope.launch {
            _loading.value = true
            // In a real app, you'd fetch user data from a repository using the current user ID
            // For now, we simulate data
            kotlinx.coroutines.delay(500)
            _userProfile.value = User(
                id = authRepository.getCurrentUser() ?: "1",
                name = "Sarah Jenkins",
                email = "sarah.j@example.com",
                phone = "555-0123",
                userType = "care_receiver"
            )
            _loading.value = false
        }
    }

    fun logout() {
        authRepository.logout()
        _logoutSuccess.value = true
    }
}
