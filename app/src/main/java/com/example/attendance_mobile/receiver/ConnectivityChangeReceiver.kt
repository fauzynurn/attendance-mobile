package com.example.attendance_mobile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository


class ConnectivityChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)) {
                val remoteRepository = RemoteRepository()
                val localRepository = LocalRepository()
                val unsentAttendance = localRepository.getListOfUnsentAttendance()
                remoteRepository.doStoreCurrentAttendance(unsentAttendance!!.map { item -> item.attendanceResponse!! }
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