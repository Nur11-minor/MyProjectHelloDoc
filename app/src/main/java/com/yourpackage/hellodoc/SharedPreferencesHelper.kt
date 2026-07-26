package com.yourpackage.hellodoc

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("HelloDocPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUser(userId: String, name: String, email: String, userType: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_TYPE, userType)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserType(): String = prefs.getString(KEY_USER_TYPE, "") ?: ""
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}