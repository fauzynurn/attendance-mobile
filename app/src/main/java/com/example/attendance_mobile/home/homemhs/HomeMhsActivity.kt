package com.example.attendance_mobile.home.homemhs

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.ScheduleMhs
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.Repository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.homemhs_layout.*

class HomeMhsActivity : AppCompatActivity(),HomeMhsContract.ViewContract{
    private val list : ArrayList<ScheduleMhs> = ArrayList()
    private lateinit var adapter : ScheduleAdapter
    override lateinit var presenter: HomeMhsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homemhs_layout)

        presenter = HomeMhsPresenter(
            this, Repository(), PermissionManager(this),SharedPreferenceHelper(this)
        )

        presenter.doFetchSummaryData()
        presenter.doFetchScheduleList()
        attendance_summary_container.startShimmer()
        schedule_list_container.startShimmer()
        schedule_list.layoutManager = LinearLayoutManager(this)
        adapter = ScheduleAdapter(list){scheduleMhs : ScheduleMhs ->
            presenter.onScheduleClick(scheduleMhs)
        }
        schedule_list.adapter = adapter
//        Handler().postDelayed({
//            attendance_summary_container.stopShimmer()
//            attendance_summary_placeholder.visibility = View.GONE
//            summary_view.visibility = View.VISIBLE
//        },3000)
    }

    override fun startBeaconActivity() {
        Snackbar.make(homemhs_parent,"NOT IMPLEMENTED YET", Snackbar.LENGTH_LONG).show()
    }

    override fun onSummaryDataLoaded(data : HashMap<String,Int>) {
        izin_num.text = data["izin"].toString()
        sakit_num.text = data["sakit"].toString()
        alpa_num.text = data["alpa"].toString()

        attendance_summary_container.stopShimmer()
        summary_view.visibility = View.VISIBLE
    }

    override fun onScheduleListLoaded(data : ArrayList<ScheduleMhs>) {
        adapter.setList(data)
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.GONE
        schedule_list.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(homemhs_parent,message, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog(message: String) {
        MaterialAlertDialogBuilder(this,R.style.DialogTheme)
            .setTitle("Pencatatan Presensi Gagal")
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }
}