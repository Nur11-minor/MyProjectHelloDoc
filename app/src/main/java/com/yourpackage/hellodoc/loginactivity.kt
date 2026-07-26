package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var userTypeDropdown: AutoCompleteTextView
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var signupLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initViews()
        setupDropdown()
        setupClickListeners()
    }

    private fun initViews() {
        userTypeDropdown = findViewById(R.id.userTypeDropdown)
        emailInput = findViewById<TextInputLayout>(R.id.emailLayout).findViewById(R.id.text_input_edit_text)
        passwordInput = findViewById<TextInputLayout>(R.id.passwordLayout).findViewById(R.id.text_input_edit_text)
        loginButton = findViewById(R.id.loginButton)
        signupLink = findViewById(R.id.signupLink)
    }

    private fun setupDropdown() {
        val userTypes = arrayOf("Care Receiver", "Caregiver (Provider)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userTypes)
        userTypeDropdown.setAdapter(adapter)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            performLogin()
        }

        signupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun performLogin() {
        val userType = userTypeDropdown.text.toString()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Validation
        if (userType.isEmpty()) {
            showError(R.id.userTypeLayout, "Please select login role")
            return
        }

        if (email.isEmpty()) {
            showError(R.id.emailLayout, "Email is required")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(R.id.emailLayout, "Please enter a valid email")
            return
        }

        if (password.isEmpty()) {
            showError(R.id.passwordLayout, "Password is required")
            return
        }

        // TODO: Implement actual login API call
        Toast.makeText(this, "Login successful as $userType!", Toast.LENGTH_SHORT).show()

        // Conditional Navigation
        if (userType.contains("Caregiver", ignoreCase = true) || userType == "Care Provider") {
            startActivity(Intent(this, DoctorDashboardActivity::class.java))
        } else {
            startActivity(Intent(this, ReceiverProfileActivity::class.java))
        }
        finish()
    }

    private fun showError(layoutId: Int, message: String) {
        val textInputLayout = findViewById<TextInputLayout>(layoutId)
        textInputLayout.error = message
        textInputLayout.requestFocus()
    }
}