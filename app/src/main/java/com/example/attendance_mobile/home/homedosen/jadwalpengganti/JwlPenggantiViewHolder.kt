package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.data.JadwalDsn
import kotlinx.android.synthetic.main.jwl_pengganti_item_layout.view.*

class JwlPenggantiViewHolder(private val v: View) : RecyclerView.ViewHolder(v), JwlPenggantiContract.ItemViewContract {

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

    override fun setKelas(kelas : String){
        v.kelas.text = kelas
    }

    override fun hideLoading(){
        v.jwlpengganti_item_loading_view.visibility = View.GONE
    }

    override fun showLoading(){
        v.jwlpengganti_item_loading_view.visibility = View.VISIBLE
    }

    override fun setDeleteBtnClickListener(item: JadwalDsn, clickListener: (JadwalDsn) -> Unit) {
        v.delete_icon.setOnClickListener { clickListener(item) }
    }

    override fun setTglDibuat(tgl: String) {
        v.tglkuliah.text = tgl
    }

    override fun setTglPengganti(tgl: String) {
        v.jwlpengganti_tglpengganti.text = tgl
    }
}