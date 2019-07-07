package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.HomeDsnActivity
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.jwl_pengganti_layout.*

class JwlPenggantiActivity : AppCompatActivity(), JwlPenggantiContract.ViewContract {
    override lateinit var presenter: JwlPenggantiPresenter
    private lateinit var jwlPenggantiAdapter: JwlPenggantiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jwl_pengganti_layout)
        presenter = JwlPenggantiPresenter(this, RemoteRepository(), SharedPreferenceHelper(this))
        presenter.doRequestJadwalPenggantiList()
        jwlpengganti_list.layoutManager = LinearLayoutManager(this)
        jwlPenggantiAdapter = JwlPenggantiAdapter(presenter)
        jwlpengganti_list.adapter = jwlPenggantiAdapter
        tambah_jwlpengganti_btn.setOnClickListener {
            startJwlPenggantiBtmSheet()
        }
        jwlpengganti_back_btn.setOnClickListener {
            startHomeDsn()
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun startHomeDsn(){
        startActivity(Intent(this, HomeDsnActivity::class.java))
        finish()
    }

    override fun reloadList(){
        no_data_found_text.visibility = View.GONE
        jwlpengganti_loading_view.visibility = View.VISIBLE
    }

    fun startJwlPenggantiBtmSheet() {
        JwlPenggantiBottomSheet(presenter)
            .show(supportFragmentManager, "jwl_pengganti_btm_sheet")
    }

    override fun hideLoading() {
        jwlpengganti_loading_view.visibility = View.GONE
    }

    override fun refreshList() {
        jwlPenggantiAdapter.notifyDataSetChanged()
    }

    override fun handleNoJwlPenggantiFound() {
        no_data_found_text.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(jwlpengganti_list_parent, message, Snackbar.LENGTH_LONG).show()
    }
}