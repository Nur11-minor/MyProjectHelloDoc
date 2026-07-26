package com.yourpackage.hellodoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourpackage.hellodoc.models.ApiResponse
import com.yourpackage.hellodoc.models.AppointmentResponse
import com.yourpackage.hellodoc.models.DoctorProfile
import com.yourpackage.hellodoc.models.DoctorStats
import com.yourpackage.hellodoc.repository.DoctorRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class DoctorProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DoctorRepository(application.applicationContext)

    // LiveData for UI state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _doctorProfile = MutableLiveData<DoctorProfile?>()
    val doctorProfile: LiveData<DoctorProfile?> = _doctorProfile

    private val _doctorStats = MutableLiveData<DoctorStats?>()
    val doctorStats: LiveData<DoctorStats?> = _doctorStats

    private val _todayAppointments = MutableLiveData<List<AppointmentResponse>>()
    val todayAppointments: LiveData<List<AppointmentResponse>> = _todayAppointments

    private val _availabilityUpdated = MutableLiveData<Boolean>()
    val availabilityUpdated: LiveData<Boolean> = _availabilityUpdated

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    fun loadDoctorData() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Check if we are using the placeholder URL
                val isPlaceholder = repository.isUsingPlaceholderUrl()

                if (isPlaceholder) {
                    // Simulate API delay
                    kotlinx.coroutines.delay(1000)
                    
                    // Stub data for development
                    _doctorProfile.value = DoctorProfile(
                        id = "1",
                        name = "Dr. Nur E Alam",
                        email = "dr.heat49@medical.com",
                        specialization = "Psychiatrist",
                        profileImage = null,
                        isAvailable = true,
                        rating = 4.8,
                        totalReviews = 128
                    )
                    
                    _doctorStats.value = DoctorStats(
                        todayAppointments = 5,
                        pendingRequests = 3,
                        totalEarnings = "$1,250",
                        totalPatients = 1247
                    )
                    
                    _todayAppointments.value = listOf(
                        AppointmentResponse("1", "John Doe", "p1", "2024-01-20", "10:00 AM", "Confirmed", "Consultation", null),
                        AppointmentResponse("2", "Jane Smith", "p2", "2024-01-20", "11:30 AM", "Pending", "Follow-up", null)
                    )
                    
                    _loading.value = false
                    return@launch
                }

                // Actual API calls for real URLs
                val profileResult = runCatching { repository.getDoctorProfile() }
                val statsResult = runCatching { repository.getDoctorStats() }
                val appointmentsResult = runCatching { repository.getTodayAppointments() }

                // Process profile
                profileResult.getOrNull()?.let { response ->
                    if (response.isSuccessful && response.body()?.success == true) {
                        _doctorProfile.value = response.body()?.data
                    }
                }

                // Process stats
                statsResult.getOrNull()?.let { response ->
                    if (response.isSuccessful && response.body()?.success == true) {
                        _doctorStats.value = response.body()?.data
                    }
                }

                // Process appointments
                appointmentsResult.getOrNull()?.let { response ->
                    if (response.isSuccessful && response.body()?.success == true) {
                        _todayAppointments.value = response.body()?.data ?: emptyList()
                    }
                }

                // If all failed or network error occurred
                if (profileResult.isFailure || statsResult.isFailure || appointmentsResult.isFailure) {
                    val errorMsg = profileResult.exceptionOrNull()?.message 
                        ?: statsResult.exceptionOrNull()?.message 
                        ?: appointmentsResult.exceptionOrNull()?.message 
                        ?: "Network error"
                    _error.value = "Network error: $errorMsg"
                }

            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateAvailability(isAvailable: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = repository.updateAvailability(isAvailable)
                if (response.isSuccessful && response.body()?.success == true) {
                    _availabilityUpdated.value = true
                    // Refresh profile to get updated status
                    loadDoctorData()
                } else {
                    _error.value = response.body()?.message ?: "Failed to update availability"
                    _availabilityUpdated.value = false
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
                _availabilityUpdated.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = repository.logout()
                if (response.isSuccessful && response.body()?.success == true) {
                    repository.clearSession()
                    _logoutSuccess.value = true
                } else {
                    _error.value = response.body()?.message ?: "Logout failed"
                    _logoutSuccess.value = false
                }
            } catch (e: Exception) {
                // Even if API fails, clear local session
                repository.clearSession()
                _logoutSuccess.value = true
                _error.value = null
            } finally {
                _loading.value = false
            }
        }
    }
}