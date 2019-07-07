package com.example.attendance_mobile.detailsummary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.detail_summary_layout.*

class DetailSummaryActivity : AppCompatActivity(),DetailSummaryContract.ViewContract{
    override lateinit var presenter: DetailSummaryPresenter
    private lateinit var adapter : DetailSummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_summary_layout)
        presenter = DetailSummaryPresenter(this, SharedPreferenceHelper(this), RemoteRepository())
        detail_summary_list_container.startShimmer()
        detail_matkul_list.layoutManager = LinearLayoutManager(this)
        adapter = DetailSummaryAdapter(presenter)
        detail_matkul_list.adapter = adapter
        detail_summary_back_btn.setOnClickListener {
            finish()
        }
        presenter.doFetchDetailSummaryList()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(detail_matkul_parent,message, Snackbar.LENGTH_LONG).show()
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    override fun onSummaryListLoaded() {
        detail_summary_list_container.stopShimmer()
        detail_summary_list_ph_container.visibility = View.GONE
        detail_matkul_list.visibility = View.VISIBLE
    }
}