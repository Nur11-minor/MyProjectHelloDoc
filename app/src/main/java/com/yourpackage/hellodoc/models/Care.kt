package com.yourpackage.hellodoc.models

data class Care(
    val providerName: String,
    val careType: String,
    val date: String,
    val status: String // Completed, In Progress
)