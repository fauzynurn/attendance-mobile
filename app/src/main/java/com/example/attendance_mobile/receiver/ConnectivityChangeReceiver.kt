package com.example.attendance_mobile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.attendance_mobile.data.request.AttendanceRequest
import com.example.attendance_mobile.data.request.ListAttendanceRequest
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.TimeUtils


class ConnectivityChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)) {
                val remoteRepository = RemoteRepository()
                val localRepository = LocalRepository()
                val unsentAttendance = localRepository.getListOfUnsentAttendance()
                val nim = SharedPreferenceHelper(context).getSharedPreferenceString("nim", "")!!
                val list = unsentAttendance!!.map { item ->
                    AttendanceRequest(
                        nim,
                        item.sesi.toString(),
                        TimeUtils.getDateInString(TimeUtils.getCurrentDate(), item.tglKuliah)
                    )
                }
                remoteRepository.doStoreCurrentAttendance(
                    ListAttendanceRequest(list)
                ) { localRepository.releaseAllExecutedItem() }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val permissionManager = PermissionManager(context)
            val cm = permissionManager.getConnectivityService()
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }

    }

}