package com.yourpackage.hellodoc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.yourpackage.hellodoc.adapters.AppointmentAdapter
import com.yourpackage.hellodoc.adapters.CareAdapter
import com.yourpackage.hellodoc.models.Appointment
import com.yourpackage.hellodoc.models.Care
import de.hdodenhof.circleimageview.CircleImageView

class ReceiverProfileActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var profileImage: CircleImageView
    private lateinit var userName: TextView
    private lateinit var greetingText: TextView
    private lateinit var totalAppointments: TextView
    private lateinit var totalCares: TextView
    private lateinit var totalProviders: TextView
    private lateinit var appointmentListView: ListView
    private lateinit var caresListView: ListView
    private lateinit var viewAllAppointments: TextView
    private lateinit var viewAllCares: TextView
    private lateinit var notificationIcon: View
    private lateinit var editProfileIcon: View

    // UI Elements
    private lateinit var btnBookAppointment: MaterialButton

    // NEW: Logout Button
    private lateinit var btnLogout: MaterialButton

    // SharedPreferences for session management
    private lateinit var sharedPreferences: SharedPreferences

    // Data
    private var appointments = mutableListOf<Appointment>()
    private var cares = mutableListOf<Care>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_profile_activity)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        initViews()
        setupClickListeners()
        loadUserData()
        loadAppointments()
        loadCares()
    }

    private fun initViews() {
        profileImage = findViewById(R.id.profileImage)
        userName = findViewById(R.id.userName)
        greetingText = findViewById(R.id.greetingText)
        totalAppointments = findViewById(R.id.totalAppointments)
        totalCares = findViewById(R.id.totalCares)
        totalProviders = findViewById(R.id.totalProviders)
        appointmentListView = findViewById(R.id.appointmentListView)
        caresListView = findViewById(R.id.caresListView)
        viewAllAppointments = findViewById(R.id.viewAllAppointments)
        viewAllCares = findViewById(R.id.viewAllCares)
        notificationIcon = findViewById(R.id.notificationIcon)
        editProfileIcon = findViewById(R.id.editProfileIcon)
        btnBookAppointment = findViewById(R.id.btnBookAppointment)

        // Initialize Logout Button
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupClickListeners() {
        notificationIcon.setOnClickListener {
            // Navigate to notifications
            Toast.makeText(this, "Opening notifications", Toast.LENGTH_SHORT).show()
        }

        editProfileIcon.setOnClickListener {
            // Navigate to edit profile
            Toast.makeText(this, "Opening edit profile", Toast.LENGTH_SHORT).show()
        }

        viewAllAppointments.setOnClickListener {
            // Navigate to all appointments
            Toast.makeText(this, "View all appointments", Toast.LENGTH_SHORT).show()
        }

        viewAllCares.setOnClickListener {
            // Navigate to all cares
            Toast.makeText(this, "View all cares received", Toast.LENGTH_SHORT).show()
        }

        btnBookAppointment.setOnClickListener {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }

        // NEW: Logout Button Click Listener
        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Quick links
        setupQuickLinks()
    }

    private fun setupQuickLinks() {
        findViewById<View>(R.id.medicalRecordsLink).setOnClickListener {
            startActivity(Intent(this, MedicalRecordsActivity::class.java))
        }
        findViewById<View>(R.id.prescriptionsLink).setOnClickListener {
            startActivity(Intent(this, PrescriptionsActivity::class.java))
        }
        findViewById<View>(R.id.testResultsLink).setOnClickListener {
            startActivity(Intent(this, TestResultsActivity::class.java))
        }
        findViewById<View>(R.id.helpSupportLink).setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }
    }

    // NEW: Logout Confirmation Dialog
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes, Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    // NEW: Perform Logout
    private fun performLogout() {
        // Clear user session data
        clearUserSession()

        // Show logout confirmation message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to Login Activity
        navigateToLogin()
    }

    // NEW: Clear User Session
    private fun clearUserSession() {
        // Clear SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // If you're using Firebase Auth, uncomment this line
        // FirebaseAuth.getInstance().signOut()
    }

    // NEW: Navigate to Login
    private fun navigateToLogin() {
        // Create intent to go to Login Activity
        // Update "LoginActivity::class.java" with your actual login activity name
        val intent = Intent(this, LoginActivity::class.java)

        // Clear the back stack and start fresh
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        // Finish current activity
        finish()
    }

    private fun loadUserData() {
        // In real app, load from SharedPreferences or API
        userName.text = "Nur E Alam"
        greetingText.text = getGreeting()
        totalAppointments.text = "12"
        totalCares.text = "8"
        totalProviders.text = "5"
    }

    private fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good Morning,"
            hour < 16 -> "Good Afternoon,"
            hour < 20 -> "Good Evening,"
            else -> "Good Night,"
        }
    }

    private fun loadAppointments() {
        // In real app, fetch from API
        appointments = mutableListOf(
            Appointment("Dr. Sarah Johnson", "Cardiologist", "Today, 2:30 PM", "Confirmed"),
            Appointment("Dr. Michael Chen", "Dermatologist", "Tomorrow, 10:00 AM", "Pending"),
            Appointment("Dr. Emily Brown", "Neurologist", "Jan 15, 3:00 PM", "Completed"),
            Appointment("Dr. Robert Wilson", "Orthopedic", "Jan 20, 11:00 AM", "Confirmed")
        )

        val adapter = AppointmentAdapter(this, appointments)
        appointmentListView.adapter = adapter

        // Set list height based on content
        appointmentListView.post {
            setListViewHeight(appointmentListView)
        }
    }

    private fun loadCares() {
        // In real app, fetch from API
        cares = mutableListOf(
            Care("Dr. Sarah Johnson", "Health Checkup", "Jan 10, 2024", "Completed"),
            Care("Dr. Michael Chen", "Skin Treatment", "Dec 28, 2023", "Completed"),
            Care("Dr. Emily Brown", "Neurology Consultation", "Dec 15, 2023", "In Progress")
        )

        val adapter = CareAdapter(cares)
        caresListView.adapter = adapter

        // Set list height based on content
        caresListView.post {
            setListViewHeight(caresListView)
        }
    }

    private fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        if (listAdapter.count == 0) {
            val params = listView.layoutParams
            params.height = 0
            listView.layoutParams = params
            return
        }

        var totalHeight = 0
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(widthMeasureSpec, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }
}