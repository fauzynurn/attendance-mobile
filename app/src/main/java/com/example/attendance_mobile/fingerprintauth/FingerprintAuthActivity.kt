package com.example.attendance_mobile.fingerprintauth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homemhs.HomeMhsActivity
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FingerprintAuthActivity : AppCompatActivity(), FingerprintAuthContract.ViewContract{
    override lateinit var presenter: FingerprintAuthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fingerprint_auth_layout)
        val intent = intent
        presenter = FingerprintAuthPresenter(this, PermissionManager(this))
        presenter.startAuth(intent.getStringExtra("macAddress"))
    }

    override fun startHome() {
        startActivity(Intent(this,HomeMhsActivity::class.java))
        finish()
    }

    override fun showDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(this,R.style.DialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") {dialog, _ ->
                dialog.dismiss()
                startHome()
            }
            .show()
    }

    override fun showToast(message : String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun setAlarm(alarmMgr : AlarmManager,macAddress : String){
        Log.i("SET ALARM","CALLED")
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.START_BACKGROUND_RANGING)
        intentFilter.addAction(Constants.BEACON_BACKGROUND_RANGING_TIMEOUT)
        intentFilter.addAction(Constants.ON_BEACON_ERROR)
        val alarmIntent = Intent(this, BeaconService::class.java).let { intent ->
            intent.putExtra("count",3)
            intent.putExtra("currentPos",0)
            intent.putExtra("macAddress",macAddress)
            PendingIntent.getService(this, 100, intent, 0)}
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2*60*1000, alarmIntent)
    }

}