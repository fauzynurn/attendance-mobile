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
//        return (android.provider.Settings.Global.getInt(
//            context.contentResolver,
//            android.provider.Settings.Global.AUTO_TIME_ZONE,
//            0
//        ) != 0
//                && android.provider.Settings.Global.getInt(
//            context.contentResolver,
//            android.provider.Settings.Global.AUTO_TIME,
//            0
//        ) != 0)
    }
}