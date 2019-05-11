package com.example.attendance_mobile.home.homemhs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.ScheduleMhs
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import kotlinx.android.synthetic.main.schedule_mhs_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAdapter(private var scheduleList: ArrayList<ScheduleMhs>, private val clickListener: (ScheduleMhs) -> Unit) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    fun setList(scheduleList: ArrayList<ScheduleMhs>){
        this.scheduleList = scheduleList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_mhs_item, parent, false)
        return ScheduleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = scheduleList[position]
        holder.bind(item,clickListener)
    }

    class ScheduleViewHolder(private val v : View) : RecyclerView.ViewHolder(v){
        fun bind(item : ScheduleMhs,clickListener: (ScheduleMhs) -> Unit){
            val currentTime : Date = Calendar.getInstance().time
            val startTime : Date = TimeUtils.convertStringToDate(item.jamMulai)

            val x = TimeUtils.addMinutesToDate(5,startTime)
            val res = currentTime < x
            v.apply {
                start_time.text = item.jamMulai
                end_time.text = item.jamSelesai
                matkul.text = item.namaMatkul
                if(item.jenisMatkul == 1) jenis_matkul.text = Constants.TEORI else jenis_matkul.text = Constants.PRAKTEK
                when{
                    currentTime >= startTime && currentTime < TimeUtils.addMinutesToDate(5,startTime) ->  {
                        status_matkul.text = "Sedang berjalan"
                        status_matkul.setTextColor(Color.parseColor("#efdd3e"))
                    }
                    currentTime > TimeUtils.addMinutesToDate(5,startTime) -> {
                        status_matkul.text = "Tidak hadir"
                        status_matkul.setTextColor(Color.parseColor("#ef463e"))
                    }
                    currentTime < startTime -> {
                        status_matkul.text = "Belum dimulai"
                        status_matkul.setTextColor(Color.parseColor("#bcbcbc"))
                    }
                }
                presence_button.setOnClickListener{clickListener(item)}
            }
        }
    }
}