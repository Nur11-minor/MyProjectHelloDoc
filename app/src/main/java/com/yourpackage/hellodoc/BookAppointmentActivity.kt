package com.yourpackage.hellodoc

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.yourpackage.hellodoc.adapters.DoctorAdapter
import com.yourpackage.hellodoc.models.RelatedDoctor

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var doctorsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        setupToolbar()
        initViews()
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