package com.example.attendance_mobile.home.homemhs.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.home.homemhs.HomeMhsContract
import kotlinx.android.synthetic.main.schedule_mhs_item.view.*

class ScheduleMhsViewHolder(private val v: View) : RecyclerView.ViewHolder(v), HomeMhsContract.ItemViewContract {
    override fun highlightItem() {
        v.apply {
            item.apply {
                setCardBackgroundColor(Color.parseColor("#3A25D4"))
                strokeWidth = 0
                strokeColor = Color.parseColor("#FFFFFF")
            }
            circle.backgroundTintList = resources.getColorStateList(R.color.white)
            start_time.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }
            end_time.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }
            matkul.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }
            jenis_matkul.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }

            status_matkul.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }
            sd.apply {
                setTextColor(Color.parseColor("#FFFFFF"))
                typeface = ResourcesCompat.getFont(v.context, R.font.productsansbold)
            }
        }
    }
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

        override fun setPresenceButtonClickListener(item : JadwalMhs, clickListener: (JadwalMhs) -> Unit) {
            v.presence_button.setOnClickListener { clickListener(item) }
        }

        override fun hidePresenceButton() {
            v.presence_button.visibility = View.GONE
        }
    }