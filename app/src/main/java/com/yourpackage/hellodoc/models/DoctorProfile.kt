package com.yourpackage.hellodoc.models

import com.google.gson.annotations.SerializedName

data class DoctorProfile(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("specialization")
    val specialization: String,

    @SerializedName("profileImage")
    val profileImage: String?,

    @SerializedName("isAvailable")
    val isAvailable: Boolean,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("totalReviews")
    val totalReviews: Int
)

data class DoctorStats(
    @SerializedName("todayAppointments")
    val todayAppointments: Int,

    @SerializedName("pendingRequests")
    val pendingRequests: Int,

    @SerializedName("totalEarnings")
    val totalEarnings: String,

    @SerializedName("totalPatients")
    val totalPatients: Int
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: T?
)

data class AppointmentResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("patientName")
    val patientName: String,

    @SerializedName("patientId")
    val patientId: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("notes")
    val notes: String?
)

data class UpdateAvailabilityRequest(
    @SerializedName("doctorId")
    val doctorId: String,

    @SerializedName("isAvailable")
    val isAvailable: Boolean
)