package com.example.attendance_mobile.home.homemhs

import android.content.Intent
import android.graphics.Color
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

    private lateinit var mhsScheduleAdapter : MhsScheduleAdapter
    override lateinit var presenter: HomeMhsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homemhs_layout)
        window.statusBarColor = Color.parseColor("#1659ce")
        window.decorView.systemUiVisibility = 0
        presenter = HomeMhsPresenter(
            this,LocalRepository(),RemoteRepository(),SharedPreferenceHelper(this)
        )
        presenter.setupHomescreen()
        presenter.doFetchSummaryData()
        presenter.doFetchScheduleList()
        attendance_summary_container.startShimmer()
        schedule_list_container.startShimmer()
        ongoing_schedule_container.startShimmer()
        schedule_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        mhsScheduleAdapter = MhsScheduleAdapter(presenter)
        schedule_list.adapter = mhsScheduleAdapter
        kehadiran_detail_matkul_ref.setOnClickListener {
            startActivity(Intent(this,DetailSummaryActivity::class.java))
        }

    }

//    fun reloadList(){
//        attendance_summary_container.startShimmer()
//        schedule_list_container.startShimmer()
//        ongoing_schedule_container.startShimmer()
//
//        no_ongoing_schedule_text.visibility = View.GONE
//        no_reguler_schedule_text.visibility = View.GONE
//        ongoing_schedule_ph_container.visibility = View.VISIBLE
//        schedule_list_ph_container.visibility = View.VISIBLE
//        schedule_list.visibility = View.GONE
//        ongoing_mhs_schedule.visibility = View.GONE
//        schedule_list_container.startShimmer()
//        ongoing_schedule_container.startShimmer()
//        presenter.doFetchSummaryData()
//        presenter.doFetchScheduleList()
//    }

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

    override fun onSummaryDataLoaded(data : HashMap<String,String>) {
        izin_num.text = data["izin"]
        sakit_num.text = data["sakit"]
        alpa_num.text = data["alpa"]

        attendance_summary_container.stopShimmer()
        summary_view.visibility = View.VISIBLE
    }

    override fun onMhsScheduleListLoaded() {
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.GONE
        schedule_list.visibility = View.VISIBLE
    }

    override fun onOngoingMhsScheduleLoaded(){
        ongoing_schedule_container.stopShimmer()
        ongoing_schedule_ph_container.visibility = View.GONE
        ongoing_mhs_schedule.visibility = View.VISIBLE
    }

    override fun handleNoMhsScheduleFound(){
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.INVISIBLE
        no_reguler_schedule_text.visibility = View.VISIBLE
    }

    override fun handleNoOngoingMhsScheduleFound(){
        ongoing_schedule_container.stopShimmer()
        ongoing_schedule_ph_container.visibility = View.GONE
        no_ongoing_schedule_text.visibility = View.VISIBLE
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

    override fun refreshScheduleList() {
        mhsScheduleAdapter.notifyDataSetChanged()
    }

    override fun checkAllRequirement() : Boolean{
        return SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)
    }

    override fun setName(name : String){
        nama.text = name
    }

    override fun setOngoingMatkulName(name: String) {
        mata_kuliah.text = name
    }

    override fun setOngoingJenisMatkul(jenisMatkul: String) {
        jenis_matkul.text = jenisMatkul
    }

    override fun setOngoingTime(startTime: String) {
        jam.text = startTime
    }
    override fun setBtnLabel(label: String) {
        presence_button.text = label
    }

    override fun setPresenceButtonClickListener(item: JadwalMhs, clickListener: (JadwalMhs) -> Unit) {
        presence_button.setOnClickListener { clickListener(item) }
    }
}
