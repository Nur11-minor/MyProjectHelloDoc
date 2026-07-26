package com.yourpackage.hellodoc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yourpackage.hellodoc.adapters.DoctorAdapter
import com.yourpackage.hellodoc.models.RelatedDoctor
import de.hdodenhof.circleimageview.CircleImageView

class CaregiverProfileActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var doctorImage: CircleImageView
    private lateinit var doctorName: TextView
    private lateinit var doctorSpecialty: TextView
    private lateinit var doctorRating: TextView
    private lateinit var totalPatients: TextView
    private lateinit var totalExperience: TextView
    private lateinit var totalAppointments: TextView
    private lateinit var satisfactionRate: TextView
    private lateinit var relatedDoctorsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.caregiver_profile_activity)

        initViews()
        setupClickListeners()
        loadDoctorData()
        loadRelatedDoctors()
    }

    private fun initViews() {
        doctorImage = findViewById(R.id.doctorImage)
        doctorName = findViewById(R.id.doctorName)
        doctorSpecialty = findViewById(R.id.doctorSpecialty)
        doctorRating = findViewById(R.id.doctorRating)
        totalPatients = findViewById(R.id.totalPatients)
        totalExperience = findViewById(R.id.totalExperience)
        totalAppointments = findViewById(R.id.totalAppointments)
        satisfactionRate = findViewById(R.id.satisfactionRate)
        relatedDoctorsListView = findViewById(R.id.relatedDoctorsListView)
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        findViewById<View>(R.id.notificationIcon).setOnClickListener {
            Toast.makeText(this, "Opening notifications", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.shareProfile).setOnClickListener {
            shareDoctorProfile()
        }

        findViewById<View>(R.id.bookAppointmentButton).setOnClickListener {
            Toast.makeText(this, "Booking appointment with ${doctorName.text}", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.viewAllDoctors).setOnClickListener {
            Toast.makeText(this, "View all similar doctors", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDoctorData() {
        // In real app, get from Intent or API
        doctorName.text = "Dr. Nur E Alam"
        doctorSpecialty.text = "Psychiatrist"
        doctorRating.text = "4.8"
        totalPatients.text = "1,247"
        totalExperience.text = "69"
        totalAppointments.text = "856"
        satisfactionRate.text = "98%"
    }

    private fun loadRelatedDoctors() {
        val doctors = listOf(
            RelatedDoctor("Dr. Sarah Johnson", "Cardiologist", "4.9", true),
            RelatedDoctor("Dr. Michael Chen", "Dermatologist", "4.7", false),
            RelatedDoctor("Dr. Emily Brown", "Neurologist", "4.8", true),
            RelatedDoctor("Dr. Robert Wilson", "Orthopedic", "4.6", true)
        )

        val adapter = DoctorAdapter(doctors)
        relatedDoctorsListView.adapter = adapter
        setListViewHeight(relatedDoctorsListView)
    }

    private fun shareDoctorProfile() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out Dr. ${doctorName.text} on HelloDoc! Specialty: ${doctorSpecialty.text}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.UNSPECIFIED
        )

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }
}