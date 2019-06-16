package com.example.attendance_mobile.beaconscanning

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance_mobile.R
import com.example.attendance_mobile.fingerprintauth.FingerprintAuthActivity
import com.example.attendance_mobile.home.homedosen.HomeDsnActivity
import com.example.attendance_mobile.home.homemhs.HomeMhsActivity
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.beacon_scanning_layout.*


class BeaconScanActivity : AppCompatActivity(), BeaconScanContract.ViewContract{
    override lateinit var presenter: BeaconScanPresenter
    override fun showDialog(title : String, message: String) {
        MaterialAlertDialogBuilder(this,R.style.DialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beacon_scanning_layout)
        presenter = BeaconScanPresenter(RemoteRepository(), LocalRepository(),SharedPreferenceHelper(this),this)
        val intent = intent
        ruangan.text = intent.getStringExtra("kodeRuangan")
        presenter.startBeaconRanging(intent.getStringExtra("macAddress"))
    }

    override fun registerReceiver(beaconReceiver: BeaconService.BeaconReceiver<BeaconScanContract.InteractorContract>) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.ON_TIMEOUT)
        intentFilter.addAction(Constants.ON_BEACON_FOUND)
        registerReceiver(beaconReceiver, intentFilter)
    }

    override fun stopService(){
        stopService(Intent(this, BeaconService::class.java))
    }

    override fun startService(macAddress: String) {
        val intent = Intent(this, BeaconService::class.java)
        intent.action = "START_RANGING"
        intent.putExtra("macAddress",macAddress)
        startService(intent)
    }

    override fun startHome(){
        finish()
    }

    override fun startFingerprintActivity(macAddress: String) {
        val intent = Intent(this,FingerprintAuthActivity::class.java)
        intent.putExtra("macAddress",macAddress)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun startHomeDsn(){
        startActivity(Intent(this,HomeDsnActivity::class.java))
        finish()
    }

    override fun startHomeMhs(){
        startActivity(Intent(this, HomeMhsActivity::class.java))
        finish()
    }



//    override fun onStop() {
//        super.onStop()
//        presenter.onShouldUnregisterReceiver()
//    }

}