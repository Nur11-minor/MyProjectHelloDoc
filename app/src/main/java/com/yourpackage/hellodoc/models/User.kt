package com.yourpackage.hellodoc.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val userType: String, // "care_receiver" or "care_provider"
    val profileImage: String? = null,
    val createdAt: String? = null
)

data class CareReceiver(
    val userId: String,
    val dob: String,
    val gender: String,
    val bloodGroup: String,
    val address: String,
    val emergencyContact: String
)

data class CareProvider(
    val userId: String,
    val specialization: String,
    val licenseNumber: String,
    val experience: Int,
    val hospital: String,
    val consultationFee: Double,
    val isVerified: Boolean = false
)