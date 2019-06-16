package com.example.attendance_mobile.model.manager

import android.app.AlarmManager
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

class PermissionManager(private val context : Context) {

    fun isPermissionGranted(type : String): Boolean {
        return context.checkSelfPermission(type) == PackageManager.PERMISSION_GRANTED
    }

    fun getImei() : String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.imei
    }

    companion object {
        fun switchBluetoothState() {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                BluetoothAdapter.getDefaultAdapter().disable()
            } else {
                BluetoothAdapter.getDefaultAdapter().enable()
            }
        }
    }


    fun isAutoTimeActive() : Boolean {
        val first = android.provider.Settings.Global.getInt(
            context.contentResolver,
            android.provider.Settings.Global.AUTO_TIME_ZONE,
            0
        )
            val second = android.provider.Settings.Global.getInt(
            context.contentResolver,
            android.provider.Settings.Global.AUTO_TIME,
            0
        )
        return first !=0 && second != 0
    }

    fun getFingerprintSystemService() : FingerprintManager{
        return context.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
    }

    fun getAlarmSystemService() : AlarmManager{
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    fun checkIfHasFingerprintEnrolled() : Boolean{
        return getFingerprintSystemService().hasEnrolledFingerprints()
    }

    fun checkIfFingerprintScanSupported() : Boolean{
        return getFingerprintSystemService().isHardwareDetected
    }

    fun getNotificationSystemService() : NotificationManager{
        return context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getConnectivityService() : ConnectivityManager{
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}