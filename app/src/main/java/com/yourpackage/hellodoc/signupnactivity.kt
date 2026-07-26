package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.yourpackage.hellodoc.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignupActivity : AppCompatActivity() {

    private lateinit var userTypeDropdown: AutoCompleteTextView
    private lateinit var nameInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var signupButton: MaterialButton
    private lateinit var loginLink: TextView
    private lateinit var termsCheckbox: CheckBox
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private var selectedUserType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        initViews()
        setupDropdowns()
        setupClickListeners()
    }

    private fun initViews() {
        userTypeDropdown = findViewById(R.id.userTypeDropdown)
        nameInput = findViewById(R.id.nameEditText)
        phoneInput = findViewById(R.id.phoneEditText)
        passwordInput = findViewById(R.id.passwordEditText)
        confirmPasswordInput = findViewById(R.id.confirmPasswordEditText)
        signupButton = findViewById(R.id.signupButton)
        loginLink = findViewById(R.id.loginLink)
        termsCheckbox = findViewById(R.id.termsCheckbox)
        progressBar = findViewById(R.id.signupProgressBar)
    }

    private fun setupDropdowns() {
        val userTypes = arrayOf("Care Receiver", "Caregiver (Provider)")
        val userTypeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userTypes)
        userTypeDropdown.setAdapter(userTypeAdapter)

        userTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedUserType = if (position == 0) "care_receiver" else "care_provider"
        }
    }

    private fun setupClickListeners() {
        signupButton.setOnClickListener {
            performSignup()
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performSignup() {
        val name = nameInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        if (selectedUserType.isEmpty()) {
            Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show()
            return
        }

        if (name.isEmpty()) {
            nameInput.error = "Name is required"
            return
        }

        if (phone.isEmpty() || phone.length < 10) {
            phoneInput.error = "Valid phone number is required"
            return
        }

        if (password.isEmpty() || password.length < 6) {
            passwordInput.error = "Password must be at least 6 characters"
            return
        }

        if (password != confirmPassword) {
            confirmPasswordInput.error = "Passwords do not match"
            return
        }

        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        // Firebase Auth requires an email. We'll use phone number as an email alias.
        val emailAlias = "${phone}@hellodoc.com"

        auth.createUserWithEmailAndPassword(emailAlias, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    saveUserToDatabase(userId, name, phone, selectedUserType)
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        signupButton.isEnabled = !isLoading
    }

    private fun saveUserToDatabase(userId: String, name: String, phone: String, userType: String) {
        val createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val user = User(
            id = userId,
            name = name,
            email = "${phone}@hellodoc.com", // Keeping consistency with auth
            phone = phone,
            userType = userType,
            createdAt = createdAt
        )

        val userRef = database.getReference("users").child(userId)
        userRef.setValue(user).addOnCompleteListener { task ->
            showLoading(false)
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                navigateToDashboard(userType)
            } else {
                Toast.makeText(this, "Failed to save profile: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToDashboard(userType: String) {
        val intent = if (userType == "care_provider") {
            Intent(this, CaregiverProfileActivity::class.java)
        } else {
            Intent(this, ReceiverProfileActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}