//package com.example.attendance_mobile.home.homemhs
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.attendance_mobile.R
//import com.example.attendance_mobile.home.homemhs.viewholder.ScheduleMhsViewHolder
//
//class AltMhsScheduleAdapter(private val presenter: HomeMhsPresenter) : RecyclerView.Adapter<ScheduleMhsViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleMhsViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_mhs_item, parent, false)
//        return ScheduleMhsViewHolder(v)
//    }
//
//    override fun getItemCount(): Int {
//        return presenter.altSize()
//    }
//
//    override fun onBindViewHolder(holder: ScheduleMhsViewHolder, position: Int) {
//        presenter.onBindScheduleItem(position,holder,2)
//    }
//}