package com.example.attendance_mobile.home.homemhs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.beaconscanning.BeaconScanActivity
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.Ruangan
import com.example.attendance_mobile.detailpersession.DetailPerSessionActivity
import com.example.attendance_mobile.detailsummary.DetailSummaryActivity
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.eyro.cubeacon.SystemRequirementManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.homemhs_layout.*

class HomeMhsActivity : AppCompatActivity(),HomeMhsContract.ViewContract{
    private lateinit var regulerAdapter : RegulerMhsScheduleAdapter
    private lateinit var altAdapter : AltMhsScheduleAdapter
    override lateinit var presenter: HomeMhsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homemhs_layout)
        presenter = HomeMhsPresenter(
            this,LocalRepository(),RemoteRepository(),SharedPreferenceHelper(this)
        )
        presenter.doFetchSummaryData()
        presenter.doFetchScheduleList()
        attendance_summary_container.startShimmer()
        schedule_list_container.startShimmer()
        alt_schedule_list_container.startShimmer()
        schedule_list.layoutManager = LinearLayoutManager(this)
        alt_schedule_list.layoutManager = LinearLayoutManager(this)
        regulerAdapter = RegulerMhsScheduleAdapter(presenter)
        altAdapter = AltMhsScheduleAdapter(presenter)
        schedule_list.adapter = regulerAdapter
        alt_schedule_list.adapter = altAdapter
        kehadiran_detail_matkul_ref.setOnClickListener {
            startActivity(Intent(this,DetailSummaryActivity::class.java))
        }

    }

    override fun startBeaconActivity(ruangan : Ruangan) {
        val intent = Intent(this, BeaconScanActivity::class.java)
        intent.putExtra("kodeRuangan",ruangan.kodeRuangan)
        intent.putExtra("macAddress",ruangan.macAddress)
        startActivity(intent)
        finish()
    }

    override fun startDetailPerSessionActivity(schedule: JadwalMhs) {
        val intent = Intent(this, DetailPerSessionActivity::class.java)
        intent.putExtra("schedule",schedule)
        startActivity(intent)
        finish()
    }

    override fun onSummaryDataLoaded(data : HashMap<String,Int>) {
        izin_num.text = data["izin"].toString()
        sakit_num.text = data["sakit"].toString()
        alpa_num.text = data["alpa"].toString()

        attendance_summary_container.stopShimmer()
        summary_view.visibility = View.VISIBLE
    }

    override fun onRegulerScheduleListLoaded() {
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.GONE
        schedule_list.visibility = View.VISIBLE
    }

    override fun onAltScheduleListLoaded(){
        alt_schedule_list_container.stopShimmer()
        alt_schedule_list_ph_container.visibility = View.GONE
        alt_schedule_list.visibility = View.VISIBLE
    }

    override fun handleNoRegulerScheduleFound(){
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.INVISIBLE
        no_reguler_schedule_text.visibility = View.VISIBLE
    }

    override fun handleNoAltScheduleFound(){
        alt_schedule_list_container.stopShimmer()
        alt_schedule_list_ph_container.visibility = View.INVISIBLE
        no_alt_schedule_text.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(homemhs_parent,message, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog(title : String, message: String) {
        MaterialAlertDialogBuilder(this,R.style.DialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun refreshRegulerList() {
        regulerAdapter.notifyDataSetChanged()
    }

    override fun refreshAltList() {
        altAdapter.notifyDataSetChanged()
    }

    override fun checkAllRequirement() : Boolean{
        return SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)
    }
}