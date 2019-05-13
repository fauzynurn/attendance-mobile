package com.example.attendance_mobile.home.homemhs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.ScheduleMhs
import kotlinx.android.synthetic.main.schedule_mhs_item.view.*

class ScheduleAdapter(private val presenter: HomeMhsPresenter) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_mhs_item, parent, false)
        return ScheduleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.size()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        presenter.onBindScheduleItem(position,holder)
    }

    class ScheduleViewHolder(private val v: View) : RecyclerView.ViewHolder(v), HomeMhsContract.ItemViewContract {

//        fun bind(item: ScheduleMhs, clickListener: (ScheduleMhs) -> Unit) {
//            val currentTime: Date = Calendar.getInstance().time
//            val startTime: Date = TimeUtils.convertStringToDate(item.jamMulai)
//
////            val x = TimeUtils.addMinutesToDate(5,startTime)
////            val res = currentTime < x
//            v.apply {
//                start_time.text = item.jamMulai
//                end_time.text = item.jamSelesai
//                matkul.text = item.namaMatkul
//                if (item.jenisMatkul == 1) jenis_matkul.text = Constants.TEORI else jenis_matkul.text =
//                    Constants.PRAKTEK
//                when {
//                    currentTime >= startTime && currentTime < TimeUtils.addMinutesToDate(
//                        Constants.LATE_LIMIT,
//                        startTime
//                    ) -> {
//                        status_matkul.text = "Sedang berjalan"
//                        status_matkul.setTextColor(Color.parseColor("#efdd3e"))
//                        presence_button.setOnClickListener { clickListener(item) }
//                    }
//                    currentTime > TimeUtils.addMinutesToDate(Constants.LATE_LIMIT, startTime) -> {
//                        status_matkul.text = "Tidak hadir"
//                        status_matkul.setTextColor(Color.parseColor("#ef463e"))
//                        presence_button.visibility = View.GONE
//                    }
//                    currentTime < startTime -> {
//                        status_matkul.text = "Belum dimulai"
//                        status_matkul.setTextColor(Color.parseColor("#bcbcbc"))
//                        presence_button.visibility = View.GONE
//                    }
//                }
//            }

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

        override fun setPresenceButtonClickListener(item : ScheduleMhs, clickListener: (ScheduleMhs) -> Unit) {
            v.presence_button.setOnClickListener { clickListener(item) }
        }

        override fun hidePresenceButton() {
            v.presence_button.visibility = View.GONE
        }
    }
}