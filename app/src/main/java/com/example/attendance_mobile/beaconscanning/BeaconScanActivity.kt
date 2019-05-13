package com.example.attendance_mobile.beaconscanning

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance_mobile.R
import com.example.attendance_mobile.fingerprintauth.FingerprintAuthActivity
import com.example.attendance_mobile.model.manager.BeaconService
import com.example.attendance_mobile.utils.Constants
import com.eyro.cubeacon.SystemRequirementManager
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
        presenter = BeaconScanPresenter(this)
        val intent = intent
        ruangan.text = intent.getStringExtra("kodeRuangan")
        presenter.startBeaconRanging(intent.getStringExtra("macAddress"))
    }

    override fun registerReceiver(beaconReceiver: BeaconService.BeaconReceiver) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.START_RANGING)
        registerReceiver(beaconReceiver, intentFilter)
    }

    override fun startService(macAddress : String) {
        startService(Intent(this, BeaconService::class.java).putExtra("macAddress",macAddress))
    }

    override fun startFingerprintActivity() {
        startActivity(Intent(this,FingerprintAuthActivity::class.java))
        finish()
    }

    override fun checkIfBluetoothActive() : Boolean{
        return SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)
    }
}