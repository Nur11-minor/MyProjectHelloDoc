package com.yourpackage.hellodoc.repository

import android.content.Context
import android.content.SharedPreferences
import com.yourpackage.hellodoc.models.ApiResponse
import com.yourpackage.hellodoc.models.AppointmentResponse
import com.yourpackage.hellodoc.models.DoctorProfile
import com.yourpackage.hellodoc.models.DoctorStats
import com.yourpackage.hellodoc.models.UpdateAvailabilityRequest
import com.yourpackage.hellodoc.network.RetrofitClient
import retrofit2.Response

class DoctorRepository(private val context: Context) {

    private val apiService = RetrofitClient.instance
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun isUsingPlaceholderUrl(): Boolean {
        // This is a simple way to check if the user has updated their API URL
        // In a real app, you'd use a BuildFlavor or Config file
        return true // For now, we stub data to prevent crashes
    }

    private fun getAuthToken(): String {
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    suspend fun getDoctorProfile(): Response<ApiResponse<DoctorProfile>> {
        return apiService.getDoctorProfile("Bearer ${getAuthToken()}")
    }

    suspend fun getDoctorStats(): Response<ApiResponse<DoctorStats>> {
        return apiService.getDoctorStats("Bearer ${getAuthToken()}")
    }

    suspend fun getTodayAppointments(): Response<ApiResponse<List<AppointmentResponse>>> {
        return apiService.getTodayAppointments("Bearer ${getAuthToken()}")
    }

    suspend fun updateAvailability(isAvailable: Boolean): Response<ApiResponse<DoctorProfile>> {
        val doctorId = sharedPreferences.getString("doctor_id", "") ?: ""
        val request = UpdateAvailabilityRequest(doctorId, isAvailable)
        return apiService.updateAvailability("Bearer ${getAuthToken()}", request)
    }

    suspend fun logout(): Response<ApiResponse<Unit>> {
        return apiService.logout("Bearer ${getAuthToken()}")
    }

    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}