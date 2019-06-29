package com.example.attendance_mobile.fingerprintauth

import android.app.AlarmManager
import com.example.attendance_mobile.BaseView

interface FingerprintAuthContract {
    interface ViewContract : BaseView<FingerprintAuthPresenter>{
        fun startHome()
        fun showDialog(title : String, message : String)
        fun showToast(message : String)
        fun setAlarm(alarmMgr : AlarmManager, macAddress : String)
    }
    interface InteractorContract{
        fun onAuthenticated()
        fun onAuthenticationFail()
        fun onAuthenticationHelp(message: String)
        fun onAuthenticationError(message : String)
    }
}