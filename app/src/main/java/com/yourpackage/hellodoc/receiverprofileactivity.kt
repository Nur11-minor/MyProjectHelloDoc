package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yourpackage.hellodoc.viewmodel.ReceiverProfileViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ReceiverProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private lateinit var greetingText: TextView
    private lateinit var totalCares: TextView
    private lateinit var totalProviders: TextView
    private lateinit var appointmentListView: ListView
    private lateinit var caresListView: ListView
    private lateinit var viewAllAppointments: TextView
    private lateinit var viewAllCares: TextView
    private lateinit var editProfileIcon: ImageView
    private lateinit var btnLogout: Button
    
    // Links
    private lateinit var medicalRecordsLink: TextView
    private lateinit var prescriptionsLink: TextView
    private lateinit var testResultsLink: TextView
    private lateinit var helpSupportLink: TextView

    private val viewModel: ReceiverProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_profile_activity)

        initViews()
        setupClickListeners()
        observeViewModel()
        
        viewModel.loadProfile()
    }

    private fun initViews() {
        profileImage = findViewById(R.id.profileImage)
        greetingText = findViewById(R.id.greetingText)
        totalCares = findViewById(R.id.totalCares)
        totalProviders = findViewById(R.id.totalProviders)
        appointmentListView = findViewById(R.id.appointmentListView)
        caresListView = findViewById(R.id.caresListView)
        viewAllAppointments = findViewById(R.id.viewAllAppointments)
        viewAllCares = findViewById(R.id.viewAllCares)
        editProfileIcon = findViewById(R.id.editProfileIcon)
        btnLogout = findViewById(R.id.btnLogout)
        
        medicalRecordsLink = findViewById(R.id.medicalRecordsLink)
        prescriptionsLink = findViewById(R.id.prescriptionsLink)
        testResultsLink = findViewById(R.id.testResultsLink)
        helpSupportLink = findViewById(R.id.helpSupportLink)
    }

    private fun setupClickListeners() {
        btnLogout.setOnClickListener {
            viewModel.logout()
        }

        editProfileIcon.setOnClickListener {
            Toast.makeText(this, "Edit profile clicked", Toast.LENGTH_SHORT).show()
        }

        medicalRecordsLink.setOnClickListener {
            Toast.makeText(this, "Opening medical records", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this) { user ->
            user?.let {
                greetingText.text = "Hello, ${it.name}"
            }
        }
        
        viewModel.loading.observe(this) { isLoading ->
            // Update UI to show loading state
        }

        viewModel.logoutSuccess.observe(this) { success ->
            if (success) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
