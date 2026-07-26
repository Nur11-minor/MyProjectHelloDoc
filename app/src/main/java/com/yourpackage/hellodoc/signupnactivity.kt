package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {

    private lateinit var userTypeDropdown: AutoCompleteTextView
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var signupButton: MaterialButton
    private lateinit var loginLink: TextView
    private lateinit var termsCheckbox: CheckBox

    // Provider fields
    private lateinit var providerFields: LinearLayout
    private lateinit var specializationDropdown: AutoCompleteTextView
    private lateinit var licenseInput: TextInputEditText
    private lateinit var experienceInput: TextInputEditText
    private lateinit var hospitalInput: TextInputEditText
    private lateinit var feeInput: TextInputEditText

    // Receiver fields
    private lateinit var receiverFields: LinearLayout
    private lateinit var dobInput: TextInputEditText
    private lateinit var genderDropdown: AutoCompleteTextView
    private lateinit var bloodGroupDropdown: AutoCompleteTextView
    private lateinit var addressInput: TextInputEditText
    private lateinit var emergencyContactInput: TextInputEditText

    private var selectedUserType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        initViews()
        setupDropdowns()
        setupClickListeners()
    }

    private fun initViews() {
        userTypeDropdown = findViewById(R.id.userTypeDropdown)
        nameInput = findViewById<TextInputLayout>(R.id.nameLayout).findViewById(R.id.text_input_edit_text)
        emailInput = findViewById<TextInputLayout>(R.id.emailLayout).findViewById(R.id.text_input_edit_text)
        phoneInput = findViewById<TextInputLayout>(R.id.phoneLayout).findViewById(R.id.text_input_edit_text)
        passwordInput = findViewById<TextInputLayout>(R.id.passwordLayout).findViewById(R.id.text_input_edit_text)
        confirmPasswordInput = findViewById<TextInputLayout>(R.id.confirmPasswordLayout).findViewById(R.id.text_input_edit_text)
        signupButton = findViewById(R.id.signupButton)
        loginLink = findViewById(R.id.loginLink)
        termsCheckbox = findViewById(R.id.termsCheckbox)

        // Provider fields
        providerFields = findViewById(R.id.providerFields)
        specializationDropdown = findViewById(R.id.specializationDropdown)
        licenseInput = findViewById<TextInputLayout>(R.id.licenseLayout).findViewById(R.id.text_input_edit_text)
        experienceInput = findViewById<TextInputLayout>(R.id.experienceLayout).findViewById(R.id.text_input_edit_text)
        hospitalInput = findViewById<TextInputLayout>(R.id.hospitalLayout).findViewById(R.id.text_input_edit_text)
        feeInput = findViewById<TextInputLayout>(R.id.feeLayout).findViewById(R.id.text_input_edit_text)

        // Receiver fields
        receiverFields = findViewById(R.id.receiverFields)
        dobInput = findViewById<TextInputLayout>(R.id.dobLayout).findViewById(R.id.text_input_edit_text)
        genderDropdown = findViewById(R.id.genderDropdown)
        bloodGroupDropdown = findViewById(R.id.bloodGroupDropdown)
        addressInput = findViewById<TextInputLayout>(R.id.addressLayout).findViewById(R.id.text_input_edit_text)
        emergencyContactInput = findViewById<TextInputLayout>(R.id.emergencyLayout).findViewById(R.id.text_input_edit_text)
    }

    private fun setupDropdowns() {
        // User Type dropdown
        val userTypes = arrayOf("Care Receiver", "Caregiver (Provider)")
        val userTypeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userTypes)
        userTypeDropdown.setAdapter(userTypeAdapter)

        userTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedUserType = userTypes[position]
            toggleFields(selectedUserType)
        }

        // Specialization dropdown for providers
        val specializations = arrayOf(
            "Cardiologist", "Dermatologist", "Neurologist",
            "Pediatrician", "Psychiatrist", "General Physician",
            "Orthopedic Surgeon", "Ophthalmologist", "Gynecologist"
        )
        val specAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specializations)
        specializationDropdown.setAdapter(specAdapter)

        // Gender dropdown
        val genders = arrayOf("Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        genderDropdown.setAdapter(genderAdapter)

        // Blood Group dropdown
        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bloodGroups)
        bloodGroupDropdown.setAdapter(bloodAdapter)
    }

    private fun toggleFields(userType: String) {
        if (userType.contains("Caregiver", ignoreCase = true) || userType == "Care Provider") {
            providerFields.visibility = LinearLayout.VISIBLE
            receiverFields.visibility = LinearLayout.GONE
        } else {
            providerFields.visibility = LinearLayout.GONE
            receiverFields.visibility = LinearLayout.VISIBLE
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

        // Date picker for DOB
        dobInput.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        // TODO: Implement DatePickerDialog
        // For now, just a placeholder
        Toast.makeText(this, "Date picker coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun performSignup() {
        // Get all values
        val userType = userTypeDropdown.text.toString()
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        // Validation
        if (userType.isEmpty()) {
            showError(R.id.userTypeLayout, "Please select user type")
            return
        }

        if (name.isEmpty()) {
            showError(R.id.nameLayout, "Full name is required")
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(R.id.emailLayout, "Please enter a valid email")
            return
        }

        if (phone.isEmpty() || phone.length < 10) {
            showError(R.id.phoneLayout, "Please enter a valid phone number")
            return
        }

        if (password.isEmpty()) {
            showError(R.id.passwordLayout, "Password is required")
            return
        }

        if (password.length < 6) {
            showError(R.id.passwordLayout, "Password must be at least 6 characters")
            return
        }

        if (password != confirmPassword) {
            showError(R.id.confirmPasswordLayout, "Passwords do not match")
            return
        }

        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
            return
        }

        // Additional validation based on user type
        if (userType.contains("Caregiver", ignoreCase = true) || userType == "Care Provider") {
            validateProviderFields()
        } else if (userType == "Care Receiver") {
            validateReceiverFields()
        }

        // TODO: Implement actual signup API call
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()

        // Navigate to login or directly to profile
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun validateProviderFields() {
        val specialization = specializationDropdown.text.toString()
        val license = licenseInput.text.toString().trim()
        val experience = experienceInput.text.toString().trim()
        val hospital = hospitalInput.text.toString().trim()
        val fee = feeInput.text.toString().trim()

        if (specialization.isEmpty()) {
            showError(R.id.specializationLayout, "Please select specialization")
            return
        }

        if (license.isEmpty()) {
            showError(R.id.licenseLayout, "Medical license number is required")
            return
        }

        if (experience.isEmpty() || experience.toIntOrNull() == null) {
            showError(R.id.experienceLayout, "Please enter valid years of experience")
            return
        }

        if (hospital.isEmpty()) {
            showError(R.id.hospitalLayout, "Hospital name is required")
            return
        }

        if (fee.isEmpty() || fee.toIntOrNull() == null) {
            showError(R.id.feeLayout, "Please enter valid consultation fee")
            return
        }
    }

    private fun validateReceiverFields() {
        val dob = dobInput.text.toString().trim()
        val gender = genderDropdown.text.toString()
        val bloodGroup = bloodGroupDropdown.text.toString()
        val address = addressInput.text.toString().trim()
        val emergency = emergencyContactInput.text.toString().trim()

        if (dob.isEmpty()) {
            showError(R.id.dobLayout, "Date of birth is required")
            return
        }

        if (gender.isEmpty()) {
            showError(R.id.genderLayout, "Please select gender")
            return
        }

        if (bloodGroup.isEmpty()) {
            showError(R.id.bloodGroupLayout, "Please select blood group")
            return
        }

        if (address.isEmpty()) {
            showError(R.id.addressLayout, "Address is required")
            return
        }

        if (emergency.isEmpty() || emergency.length < 10) {
            showError(R.id.emergencyLayout, "Please enter valid emergency contact")
            return
        }
    }

    private fun showError(layoutId: Int, message: String) {
        val textInputLayout = findViewById<TextInputLayout>(layoutId)
        textInputLayout.error = message
        textInputLayout.requestFocus()
    }
}