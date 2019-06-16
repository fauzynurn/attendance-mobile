package com.example.attendance_mobile.home.homedosen.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.home.homedosen.mhslist.MhsListContract
import kotlinx.android.synthetic.main.mhs_list_item.view.*

class MhsListViewHolder(private val v: View) : RecyclerView.ViewHolder(v), MhsListContract.ItemViewContract {
    override fun setNama(nama : String) {
        v.nama_mhs.text = nama
    }

    override fun setHadir() {
        v.status_hadir.apply {
            text = "Hadir"
            setTextColor(Color.parseColor("#80f427"))
        }
    }

    override fun setTidakHadir() {
        v.status_hadir.apply {
            text = "Tidak hadir"
            setTextColor(Color.parseColor("#fc6767"))
        }
    }

}