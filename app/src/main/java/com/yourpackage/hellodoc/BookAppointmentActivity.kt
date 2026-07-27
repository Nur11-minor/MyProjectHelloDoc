package com.yourpackage.hellodoc

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yourpackage.hellodoc.adapters.DoctorAdapter
import com.yourpackage.hellodoc.models.RelatedDoctor

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var doctorsListView: ListView
    private lateinit var searchEditText: MaterialAutoCompleteTextView
    
    private val specialties = arrayOf(
        "Cardiology", "Dermatology", "Neurology", "Orthopedics", 
        "Pediatrics", "Psychiatry", "Gynecology", "Ophthalmology", 
        "Urology", "Gastroenterology", "Oncology", "Endocrinology",
        "Dentistry", "ENT Specialist", "General Physician", "Radiology",
        "Hematology", "Nephrology", "Pulmonology", "Rheumatology"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        setupToolbar()
        initViews()
        setupSearchSuggestions()
        loadTopDoctors()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initViews() {
        doctorsListView = findViewById(R.id.doctorsListView)
        searchEditText = findViewById(R.id.searchEditText)
    }

    private fun setupSearchSuggestions() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specialties)
        searchEditText.setAdapter(adapter)

        searchEditText.setOnItemClickListener { parent, _, position, _ ->
            val selectedSpecialty = parent.getItemAtPosition(position) as String
            filterDoctors(selectedSpecialty)
        }
    }

    private fun filterDoctors(query: String) {
        // In a real app, you would filter your data source here
        // For now, we'll just show a toast or reload with dummy filtered data
        android.widget.Toast.makeText(this, "Searching for: $query", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun loadTopDoctors() {
        val doctors = listOf(
            RelatedDoctor("Dr. Nur E Alam", "Psychiatrist", "4.8", true),
            RelatedDoctor("Dr. Sarah Johnson", "Cardiologist", "4.9", true),
            RelatedDoctor("Dr. Michael Chen", "Dermatologist", "4.7", false),
            RelatedDoctor("Dr. Emily Brown", "Neurologist", "4.8", true),
            RelatedDoctor("Dr. Robert Wilson", "Orthopedic", "4.6", true)
        )

        val adapter = DoctorAdapter(doctors)
        doctorsListView.adapter = adapter
        setListViewHeight(doctorsListView)
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