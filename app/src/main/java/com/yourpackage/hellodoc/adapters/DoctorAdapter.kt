package com.yourpackage.hellodoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yourpackage.hellodoc.R
import com.yourpackage.hellodoc.models.RelatedDoctor
import de.hdodenhof.circleimageview.CircleImageView

class DoctorAdapter(
    private val doctors: List<RelatedDoctor>
) : BaseAdapter() {

    override fun getCount(): Int = doctors.size
    override fun getItem(position: Int): Any = doctors[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_doctor, parent, false)

        val doctor = doctors[position]

        view.findViewById<CircleImageView>(R.id.ivDoctor).setImageResource(R.drawable.profile_pic)
        view.findViewById<TextView>(R.id.tvDoctorName).text = doctor.name
        view.findViewById<TextView>(R.id.tvSpecialty).text = doctor.specialty
        view.findViewById<TextView>(R.id.tvRating).text = "★ ${doctor.rating}"

        val availabilityView = view.findViewById<TextView>(R.id.tvAvailability)
        if (doctor.isAvailable) {
            availabilityView.text = "● Available"
            availabilityView.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
        } else {
            availabilityView.text = "● Unavailable"
            availabilityView.setTextColor(android.graphics.Color.parseColor("#F44336"))
        }

        return view
    }
}