package com.example.attendance_mobile.home.homedosen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.viewholder.ScheduleDsnViewHolder

class AltDsnScheduleAdapter(private val presenter: HomeDsnPresenter) : RecyclerView.Adapter<ScheduleDsnViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDsnViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_mhs_item, parent, false)
        return ScheduleDsnViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.altSize()
    }

    override fun onBindViewHolder(holder: ScheduleDsnViewHolder, position: Int) {
        presenter.onBindScheduleItem(position,holder,2)
    }
}