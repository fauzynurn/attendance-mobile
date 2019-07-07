package com.example.attendance_mobile.home.homemhs.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.home.homemhs.HomeMhsContract
import kotlinx.android.synthetic.main.schedule_mhs_item.view.*

class ScheduleMhsViewHolder(private val v: View) : RecyclerView.ViewHolder(v), HomeMhsContract.ItemViewContract {

    override fun setStartTime(startTime: String) {
        v.start_time.text = startTime
    }

    override fun setEndTime(endTime: String) {
        v.end_time.text = endTime
    }

    override fun setNamaMatkul(namaMatkul: String) {
        v.matkul.text = namaMatkul
    }

    override fun setJenisMatkul(jenisMatkul: String) {
        v.jenis_matkul.text = jenisMatkul
    }

}
