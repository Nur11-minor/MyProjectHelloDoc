package com.yourpackage.hellodoc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yourpackage.hellodoc.R
import com.yourpackage.hellodoc.models.Appointment

class AppointmentAdapter(
    private val context: Context,
    private val appointments: MutableList<Appointment>
) : BaseAdapter() {
    // ... adapter implementation

    override fun getCount(): Int = appointments.size
    override fun getItem(position: Int): Any = appointments[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_appointment, parent, false)

        val appointment = appointments[position]

        view.findViewById<TextView>(R.id.tvDoctorName).text = appointment.name
        view.findViewById<TextView>(R.id.tvSpecialty).text = appointment.subText
        view.findViewById<TextView>(R.id.tvDateTime).text = appointment.dateTime

        val statusView = view.findViewById<TextView>(R.id.tvStatus)
        statusView.text = appointment.status
        when (appointment.status.lowercase()) {
            "confirmed" -> statusView.setBackgroundResource(R.drawable.status_confirmed)
            "pending" -> statusView.setBackgroundResource(R.drawable.status_pending)
            "completed" -> statusView.setBackgroundResource(R.drawable.status_completed)
            "cancelled" -> statusView.setBackgroundResource(R.drawable.status_cancelled)
        }

        return view
    }
}