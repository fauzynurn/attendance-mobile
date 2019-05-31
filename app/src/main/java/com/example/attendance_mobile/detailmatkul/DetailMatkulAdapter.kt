package com.example.attendance_mobile.detailmatkul

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import kotlinx.android.synthetic.main.detail_summary_item.view.*

class DetailMatkulAdapter(private val presenter: DetailMatkulPresenter) : RecyclerView.Adapter<DetailMatkulAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.detail_summary_item, parent, false)
        return ScheduleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.size()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        presenter.onBindDetailSummaryItem(position,holder)
    }

    class ScheduleViewHolder(private val v: View) : RecyclerView.ViewHolder(v), DetailMatkulContract.ItemViewContract {

        override fun setNamaMatkul(namaMatkul: String) {
            v.detail_nama_matkul.text = namaMatkul
        }

        override fun setJenisMatkul(jenisMatkul: String) {
            v.detail_jenis_matkul.text = jenisMatkul
        }

        override fun setJmlhHadir(jmlhHadir: Int) {
            v.jumlah_hadir.text = jmlhHadir.toString()
        }

        override fun setJmlhTdkHadir(jmlhTdkHadir: Int) {
            v.jumlah_tidak_hadir.text = jmlhTdkHadir.toString()
        }

    }
}