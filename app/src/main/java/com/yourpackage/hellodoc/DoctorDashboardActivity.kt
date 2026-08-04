package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.yourpackage.hellodoc.adapters.AppointmentAdapter
import com.yourpackage.hellodoc.models.Appointment
import com.yourpackage.hellodoc.viewmodel.DoctorProfileViewModel
import de.hdodenhof.circleimageview.CircleImageView

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var profileThumbnail: CircleImageView
    private lateinit var doctorName: TextView
    private lateinit var welcomeText: TextView
    private lateinit var todayAppointments: TextView
    private lateinit var pendingRequests: TextView
    private lateinit var totalEarnings: TextView
    private lateinit var avgRating: TextView
    private lateinit var appointmentListView: ListView
    private lateinit var viewAllAppointments: TextView
    private lateinit var availabilitySwitch: SwitchCompat

    private lateinit var btnPatients: MaterialButton
    private lateinit var btnRecords: MaterialButton
    private lateinit var btnSettings: MaterialButton
    private lateinit var btnLogout: MaterialButton

    private var progressIndicator: CircularProgressIndicator? = null

    private val viewModel: DoctorProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doctor_dashboard_activity)

        initViews()
        setupClickListeners()
        observeViewModel()

        viewModel.loadDoctorData()
    }

    private fun initViews() {
        profileThumbnail = findViewById(R.id.profileThumbnail)
        doctorName = findViewById(R.id.doctorName)
        welcomeText = findViewById(R.id.welcomeText)
        todayAppointments = findViewById(R.id.todayAppointments)
        pendingRequests = findViewById(R.id.pendingRequests)
        totalEarnings = findViewById(R.id.totalEarnings)
        avgRating = findViewById(R.id.avgRating)
        appointmentListView = findViewById(R.id.dashboardAppointmentList)
        viewAllAppointments = findViewById(R.id.viewAllAppointments)
        availabilitySwitch = findViewById(R.id.availabilitySwitch)

        btnPatients = findViewById(R.id.btnPatients)
        btnRecords = findViewById(R.id.btnRecords)
        btnSettings = findViewById(R.id.btnSettings)
        btnLogout = findViewById(R.id.btnLogout)

        progressIndicator = findViewById(R.id.progressIndicator)
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.doctorProfile.observe(this) { profile ->
            profile?.let {
                doctorName.text = it.name
                welcomeText.text = getGreeting()
                availabilitySwitch.isChecked = it.isAvailable
                avgRating.text = it.rating.toString()
            }
        }

        viewModel.doctorStats.observe(this) { stats ->
            stats?.let {
                todayAppointments.text = it.todayAppointments.toString()
                pendingRequests.text = it.pendingRequests.toString()
                totalEarnings.text = it.totalEarnings
            }
        }

        viewModel.todayAppointments.observe(this) { appointments ->
            val appointmentModels = appointments.map { appointment ->
                Appointment(
                    name = appointment.patientName,
                    subText = appointment.type,
                    dateTime = "${appointment.date}, ${appointment.time}",
                    status = appointment.status
                )
            }

            val adapter = AppointmentAdapter(this, appointmentModels.toMutableList())
            appointmentListView.adapter = adapter
            appointmentListView.post {
                setListViewHeight(appointmentListView)
            }
        }

        viewModel.logoutSuccess.observe(this) { success ->
            if (success) {
                navigateToLogin()
            }
        }
    }

    private fun setupClickListeners() {
        availabilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateAvailability(isChecked)
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
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

    private fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return
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

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes, Logout") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
