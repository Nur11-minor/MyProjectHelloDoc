package com.yourpackage.hellodoc.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yourpackage.hellodoc.models.User
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeoutException

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "AuthRepository"
        private const val TIMEOUT_MS = 10000L // 10 seconds timeout
    }

    suspend fun signup(user: User, password: String): Result<User> {
        return try {
            withTimeout(TIMEOUT_MS) {
                Log.d(TAG, "Starting signup for ${user.email}")
                val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID from Auth")
                
                Log.d(TAG, "Auth successful, user ID: $userId. Saving to Firestore...")
                val updatedUser = user.copy(id = userId)
                
                firestore.collection("users")
                    .document(userId)
                    .set(updatedUser)
                    .await()
                
                Log.d(TAG, "Firestore save successful for $userId")
                Result.success(updatedUser)
            }
        } catch (e: Exception) {
            val errorMsg = if (e is kotlinx.coroutines.TimeoutCancellationException) {
                "Operation timed out. Please check your internet connection."
            } else {
                e.message ?: "Unknown error occurred"
            }
            Log.e(TAG, "Signup failed: $errorMsg", e)
            Result.failure(Exception(errorMsg))
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            withTimeout(TIMEOUT_MS) {
                Log.d(TAG, "Starting login for $email")
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID from Auth")
                
                Log.d(TAG, "Auth successful, user ID: $userId. Fetching from Firestore...")
                val userSnapshot = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()
                
                val user = userSnapshot.toObject(User::class.java) ?: throw Exception("User data not found in Firestore")
                
                Log.d(TAG, "Firestore fetch successful for $userId")
                Result.success(user)
            }
        } catch (e: Exception) {
            val errorMsg = if (e is kotlinx.coroutines.TimeoutCancellationException) {
                "Operation timed out. Please check your internet connection."
            } else {
                e.message ?: "Unknown error occurred"
            }
            Log.e(TAG, "Login failed: $errorMsg", e)
            Result.failure(Exception(errorMsg))
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): String? = auth.currentUser?.uid
}
