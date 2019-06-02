package com.example.attendance_mobile.fingerprintauth

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homemhs.HomeMhsActivity
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.service.BeaconBackgroundService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.realm.Realm

class FingerprintAuthActivity : AppCompatActivity(), FingerprintAuthContract.ViewContract{
    override lateinit var presenter: FingerprintAuthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fingerprint_auth_layout)
        val intent = intent
        presenter = FingerprintAuthPresenter(LocalRepository(Realm.getDefaultInstance()),this, PermissionManager(this))
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

    override fun setAlarm(alarmMgr : AlarmManager,macAddress : String, timeExecution : Long){
        Log.i("SET ALARM","CALLED")
        val intent = Intent(this, BeaconBackgroundService::class.java)
        intent.apply {
            putExtra("macAddress",macAddress)
            putExtra("time_execution",timeExecution.toString())
            action = "INIT_RANGING"
        }
        startService(intent)
    }

}