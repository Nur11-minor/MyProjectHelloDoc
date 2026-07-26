package com.yourpackage.hellodoc.network

import com.yourpackage.hellodoc.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("api/user/profile/{userId}")
    fun getUserProfile(@Path("userId") userId: String): Call<UserProfileResponse>

    @GET("api/appointments/{userId}")
    fun getAppointments(@Path("userId") userId: String): Call<List<AppointmentResponse>>

    @GET("api/doctors/recommended")
    fun getRecommendedDoctors(): Call<List<DoctorResponse>>

    // Doctor specific (Suspend functions)
    @GET("api/doctor/profile")
    suspend fun getDoctorProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<DoctorProfile>>

    @GET("api/doctor/stats")
    suspend fun getDoctorStats(
        @Header("Authorization") token: String
    ): Response<ApiResponse<DoctorStats>>

    @GET("api/doctor/appointments/today")
    suspend fun getTodayAppointments(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<AppointmentResponse>>>

    @PUT("api/doctor/availability")
    suspend fun updateAvailability(
        @Header("Authorization") token: String,
        @Body request: UpdateAvailabilityRequest
    ): Response<ApiResponse<DoctorProfile>>

    @POST("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>
}

// Data Classes
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val userType: String,
    val providerDetails: ProviderDetails?,
    val receiverDetails: ReceiverDetails?
)

data class ProviderDetails(
    val specialization: String,
    val licenseNumber: String,
    val experience: Int,
    val hospital: String,
    val consultationFee: Double
)

data class ReceiverDetails(
    val dob: String,
    val gender: String,
    val bloodGroup: String,
    val address: String,
    val emergencyContact: String
)

data class LoginResponse(val success: Boolean, val message: String, val user: User)
data class User(val id: String, val name: String, val email: String, val userType: String)
data class RegisterResponse(val success: Boolean, val message: String)

data class UserProfileResponse(
    val success: Boolean,
    val message: String,
    val user: User,
    val providerDetails: ProviderDetails?,
    val receiverDetails: ReceiverDetails?
)

data class DoctorResponse(
    val id: String,
    val name: String,
    val specialization: String,
    val hospital: String,
    val rating: Double,
    val consultationFee: Double,
    val imageUrl: String
)
