package com.yourpackage.hellodoc.models

data class Appointment(
    val name: String,
    val subText: String,
    val dateTime: String,
    val status: String // Confirmed, Pending, Completed, Cancelled
)