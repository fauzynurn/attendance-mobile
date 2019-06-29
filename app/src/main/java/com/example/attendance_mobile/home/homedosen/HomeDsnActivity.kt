package com.example.attendance_mobile.home.homedosen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.R
import com.example.attendance_mobile.beaconscanning.BeaconScanActivity
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.home.homedosen.jadwalpengganti.JwlPenggantiBottomSheet
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
    private lateinit var regulerAdapter: RegulerDsnScheduleAdapter
    private lateinit var altAdapter: AltDsnScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homedsn_layout)
        presenter = HomeDsnPresenter(this, RemoteRepository(), SharedPreferenceHelper(this))
        presenter.setupHomescreen()
        reloadList()
        dsn_reguler_schedule_list.layoutManager = LinearLayoutManager(this)
        dsn_alt_schedule_list.layoutManager = LinearLayoutManager(this)
        regulerAdapter = RegulerDsnScheduleAdapter(presenter)
        altAdapter = AltDsnScheduleAdapter(presenter)
        dsn_reguler_schedule_list.adapter = regulerAdapter
        dsn_alt_schedule_list.adapter = altAdapter
        jwl_pengganti_btn.setOnClickListener {
            startJwlPenggantiBtmSheet()
        }
    }

    fun reloadList(){
        dsn_schedule_list_ph_container.visibility = View.VISIBLE
        dsn_reguler_schedule_list.visibility = View.GONE
        dsn_alt_schedule_list_ph_container.visibility = View.VISIBLE
        dsn_alt_schedule_list.visibility = View.GONE
        dsn_schedule_list_container.startShimmer()
        dsn_alt_schedule_list_container.startShimmer()
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

    override fun startMhsListActivity() {
        val intent = Intent(this, MhsListActivity::class.java)
//        intent.putExtra("kodeRuangan", kodeRuangan)
//        intent.putExtra("macAddress", macAddress)
        startActivity(intent)
    }

    override fun onRegulerScheduleListLoaded() {
        dsn_schedule_list_container.stopShimmer()
        dsn_schedule_list_ph_container.visibility = View.GONE
        dsn_reguler_schedule_list.visibility = View.VISIBLE
    }

    override fun onAltScheduleListLoaded() {
        dsn_alt_schedule_list_container.stopShimmer()
        dsn_alt_schedule_list_ph_container.visibility = View.GONE
        dsn_alt_schedule_list.visibility = View.VISIBLE
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

    override fun startJwlPenggantiBtmSheet() {
        JwlPenggantiBottomSheet(presenter)
            .show(supportFragmentManager, "jwl_pengganti_btm_sheet")
    }

    override fun refreshRegulerList() {
        regulerAdapter.notifyDataSetChanged()
    }

    override fun refreshAltList() {
        altAdapter.notifyDataSetChanged()
    }

    override fun checkAllRequirement(): Boolean {
        return SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)
    }

    override fun handleNoRegulerScheduleFound() {
        dsn_schedule_list_container.stopShimmer()
        dsn_schedule_list_ph_container.visibility = View.INVISIBLE
        dsn_no_reguler_schedule_text.visibility = View.VISIBLE
    }

    override fun handleNoAltScheduleFound() {
        dsn_alt_schedule_list_container.stopShimmer()
        dsn_alt_schedule_list_ph_container.visibility = View.INVISIBLE
        dsn_no_alt_schedule_text.visibility = View.VISIBLE
    }

    override fun setName(name : String){
        nama.text = name
    }
}