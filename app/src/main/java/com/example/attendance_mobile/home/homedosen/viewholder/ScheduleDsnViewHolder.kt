package com.example.attendance_mobile.home.homedosen.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.data.DsnSchedule
import com.example.attendance_mobile.home.homedosen.HomeDsnContract
import kotlinx.android.synthetic.main.schedule_mhs_item.view.*

class ScheduleDsnViewHolder(private val v: View) : RecyclerView.ViewHolder(v), HomeDsnContract.ItemViewContract {

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

        override fun setStatusMatkul(statusMatkul: String) {
            v.status_matkul.text = statusMatkul
        }

        override fun setStatusMatkulColor(colorCode: String) {
            v.status_matkul.setTextColor(Color.parseColor(colorCode))
        }

        override fun setArrowButtonClickListener(item : DsnSchedule, clickListener: (DsnSchedule) -> Unit) {
            v.presence_button.setOnClickListener { clickListener(item) }
        }

        override fun hidePresenceButton() {
            v.presence_button.visibility = View.GONE
        }
    }