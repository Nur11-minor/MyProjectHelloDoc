package com.yourpackage.hellodoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yourpackage.hellodoc.R
import com.yourpackage.hellodoc.models.Care

class CareAdapter(
    private val cares: List<Care>
) : BaseAdapter() {

    override fun getCount(): Int = cares.size
    override fun getItem(position: Int): Any = cares[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_care_received, parent, false)

        val care = cares[position]

        view.findViewById<TextView>(R.id.tvProviderName).text = care.providerName
        view.findViewById<TextView>(R.id.tvCareType).text = care.careType
        view.findViewById<TextView>(R.id.tvDate).text = care.date

        val statusView = view.findViewById<TextView>(R.id.tvCareStatus)
        statusView.text = care.status
        when (care.status.lowercase()) {
            "completed" -> statusView.setBackgroundResource(R.drawable.status_completed)
            "in progress" -> statusView.setBackgroundResource(R.drawable.status_pending)
        }

        return view
    }
}