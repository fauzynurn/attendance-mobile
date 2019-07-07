package com.example.attendance_mobile.home.homedosen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.beaconscanning.BeaconScanActivity
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.home.homedosen.jadwalpengganti.JwlPenggantiActivity
import com.example.attendance_mobile.home.homedosen.mhslist.MhsListActivity
import com.example.attendance_mobile.home.homedosen.startclass.startschedule.StartScheduleBtmSheet
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.eyro.cubeacon.SystemRequirementManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.homedsn_layout.*


class HomeDsnActivity : AppCompatActivity(), HomeDsnContract.ViewContract {
    override lateinit var presenter: HomeDsnPresenter
    private lateinit var scheduleAdapter: DsnScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homedsn_layout)
        window.statusBarColor = Color.parseColor("#1659ce")
        window.decorView.systemUiVisibility = 0
        presenter = HomeDsnPresenter(this, RemoteRepository(), SharedPreferenceHelper(this))
        presenter.setupHomescreen()
        reloadList()
        schedule_list.layoutManager = LinearLayoutManager(this)
        scheduleAdapter = DsnScheduleAdapter(presenter)
        schedule_list.adapter = scheduleAdapter
        jwl_pengganti_btn.setOnClickListener {
            startJwlPenggantiActivity()
        }
    }

    fun startJwlPenggantiActivity(){
        val intent = Intent(this, JwlPenggantiActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun reloadList(){
        no_ongoing_schedule_text.visibility = View.GONE
        no_dsn_schedule_text.visibility = View.GONE
        ongoing_dsn_schedule_ph_container.visibility = View.VISIBLE
        schedule_list_ph_container.visibility = View.VISIBLE
        schedule_list.visibility = View.GONE
        ongoing_dsn_schedule.visibility = View.GONE
        schedule_list_container.startShimmer()
        ongoing_dsn_schedule_container.startShimmer()
        presenter.doFetchScheduleList()
    }

    override fun startBeaconActivity(kodeRuangan: String, macAddress: String) {
        val intent = Intent(this, BeaconScanActivity::class.java)
        intent.putExtra("kodeRuangan", kodeRuangan)
        intent.putExtra("macAddress", macAddress)
        startActivity(intent)
        finish()
    }

    override fun openStartScheduleBtmSheet(dsnSchedule: JadwalDsn) {
        val bundle = Bundle()
        bundle.putSerializable("dsnSchedule",dsnSchedule)
        val startScheduleBottomSheet =
            StartScheduleBtmSheet()
        startScheduleBottomSheet.arguments = bundle
        startScheduleBottomSheet.show(supportFragmentManager, "start_schedule_btm_sheet")
    }

    override fun startMhsListActivity(kelas : String) {
        val intent = Intent(this, MhsListActivity::class.java)
        intent.putExtra("kelas", kelas)
//        intent.putExtra("macAddress", macAddress)
        startActivity(intent)
    }

    override fun onDsnScheduleListLoaded() {
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.GONE
        schedule_list.visibility = View.VISIBLE
    }

    override fun onOngoingDsnScheduleLoaded(){
        ongoing_dsn_schedule_container.stopShimmer()
        ongoing_dsn_schedule_ph_container.visibility = View.GONE
        ongoing_dsn_schedule.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(homedsn_parent, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(this, R.style.DialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun refreshScheduleList() {
        scheduleAdapter.notifyDataSetChanged()
    }


    override fun checkAllRequirement(): Boolean {
        return SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)
    }

    override fun handleNoDsnScheduleFound() {
        schedule_list_container.stopShimmer()
        schedule_list_ph_container.visibility = View.INVISIBLE
        no_dsn_schedule_text.visibility = View.VISIBLE
    }

    override fun handleNoOngoingDsnScheduleFound() {
        ongoing_dsn_schedule_container.stopShimmer()
        ongoing_dsn_schedule_ph_container.visibility = View.GONE
        no_ongoing_schedule_text.visibility = View.VISIBLE
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

    override fun setOngoingKelas(kelasString: String) {
        kelas.text = kelasString
    }

    override fun setPresenceButtonClickListener(item: JadwalDsn, clickListener: (JadwalDsn) -> Unit) {
        presence_button.setOnClickListener { clickListener(item) }
    }
}