package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.yourpackage.hellodoc.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var userTypeDropdown: AutoCompleteTextView
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var signupLink: TextView
    private lateinit var progressBar: ProgressBar

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initViews()
        setupDropdown()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        userTypeDropdown = findViewById(R.id.userTypeDropdown)
        emailInput = findViewById(R.id.emailEditText)
        passwordInput = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signupLink = findViewById(R.id.signupLink)
        progressBar = findViewById(R.id.loginProgressBar)
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

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> showLoading(true)
                is LoginViewModel.LoginState.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard(state.user.userType)
                }
                is LoginViewModel.LoginState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun performLogin() {
        val emailOrPhone = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (emailOrPhone.isEmpty()) {
            emailInput.error = "Email or Phone is required"
            return
        }

        if (password.isEmpty()) {
            passwordInput.error = "Password is required"
            return
        }

        viewModel.login(emailOrPhone, password)
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        loginButton.isEnabled = !isLoading
    }

    private fun navigateToDashboard(userType: String) {
        val intent = if (userType == "care_provider") {
            Intent(this, DoctorDashboardActivity::class.java)
        } else {
            Intent(this, ReceiverProfileActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
