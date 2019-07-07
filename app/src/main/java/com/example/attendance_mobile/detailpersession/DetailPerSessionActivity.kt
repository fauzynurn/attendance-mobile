package com.example.attendance_mobile.detailpersession

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.home.homemhs.HomeMhsActivity
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.tooltip.Tooltip
import kotlinx.android.synthetic.main.detail_per_session_layout.*

class DetailPerSessionActivity : AppCompatActivity(), DetailPerSessionContract.ViewContract {
    private lateinit var adapter: DetailPerSessionAdapter
    override lateinit var presenter: DetailPerSessionPresenter

    override fun setNamaMatkul(nama: String) {
        mata_kuliah.text = nama
    }

    override fun setJamMatkul(jamMulai: String, jamSelesai: String) {
        jam.text = "$jamMulai - $jamSelesai"
    }

    override fun setDosen(dosen: String) {
        nama_dosen.text = dosen
    }

    override fun hideMoreButton() {
        more_button.visibility = View.GONE
    }

    override fun setMoreClickListener(list : List<String>){
        more_button.setOnClickListener {
            Tooltip.Builder(it,R.style.tooltipStyle)
                .setText(list.joinToString { nama ->
                    nama
                })
                .setPadding(18)
                .setCancelable(true)
                .show()
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun startHomeMhs(){
        startActivity(Intent(this, HomeMhsActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_per_session_layout)
        presenter = DetailPerSessionPresenter(this, RemoteRepository(), SharedPreferenceHelper(this))
        session_list.layoutManager = LinearLayoutManager(this)
        adapter = DetailPerSessionAdapter(presenter)
        session_list.adapter = adapter
        val schedule = intent.getSerializableExtra("schedule") as JadwalMhs
        presenter.onActivityStart(schedule)
        detail_kehadiran_back_btn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onAttendanceSessionListLoaded() {
        Log.i("nnn", "not implemented")
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }
}