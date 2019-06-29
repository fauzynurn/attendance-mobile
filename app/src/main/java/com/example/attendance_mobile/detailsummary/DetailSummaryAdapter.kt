package com.example.attendance_mobile.detailsummary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import kotlinx.android.synthetic.main.detail_summary_item.view.*

class DetailSummaryAdapter(private val presenter: DetailSummaryPresenter) : RecyclerView.Adapter<DetailSummaryAdapter.DetailSummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSummaryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.detail_summary_item, parent, false)
        return DetailSummaryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.size()
    }

    override fun onBindViewHolder(holder: DetailSummaryViewHolder, position: Int) {
        presenter.onBindItem(position,holder)
    }

    class DetailSummaryViewHolder(private val v: View) : RecyclerView.ViewHolder(v), DetailSummaryContract.ItemViewContract {

        override fun setNamaMatkul(namaMatkul: String) {
            v.detail_nama_matkul.text = namaMatkul
        }

        override fun setJenisMatkul(jenisMatkul: String) {
            v.detail_jenis_matkul.text = jenisMatkul
        }

        override fun setJmlhHadir(jmlhHadir: String) {
            v.jumlah_hadir.text = jmlhHadir
        }

        override fun setJmlhTdkHadir(jmlhTdkHadir: String) {
            v.jumlah_tidak_hadir.text = jmlhTdkHadir
        }

    }
}