package com.example.attendance_mobile.model.manager

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager

class PermissionManager(private val context : Context) {

    fun isPermissionGranted(type : String): Boolean {
        return context.checkSelfPermission(type) == PackageManager.PERMISSION_GRANTED
    }

    fun getImei() : String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.imei
    }
}