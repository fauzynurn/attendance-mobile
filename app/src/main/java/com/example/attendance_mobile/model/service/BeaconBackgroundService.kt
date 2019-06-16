package com.example.attendance_mobile.model.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.NotificationManager
import com.example.attendance_mobile.utils.TimeUtils
import com.example.attendance_mobile.utils.WakeLocker
import com.eyro.cubeacon.*
import io.realm.Realm
import java.util.*

class BeaconBackgroundService : CBServiceListener, CBRangingListener, Service() {
    private lateinit var cubeacon: Cubeacon
    private lateinit var region: CBRegion
    private lateinit var handler: Handler
    private var macAddress: String = ""
    private lateinit var localRepository: LocalRepository
    private lateinit var remoteRepository : RemoteRepository
    private lateinit var startTime: Date
    private lateinit var endTime: Date
    private lateinit var realm : Realm

    override fun didRangeBeaconsInRegion(p0: MutableList<CBBeacon>?, p1: CBRegion?) {
        if (p0!!.size > 0 && p0.any { it.macAddress == macAddress }) {
            PermissionManager.switchBluetoothState()
            Log.i("XX", "RANGE_IN_REGION_CALLED")
            handler.removeCallbacksAndMessages(null)
            cubeacon.stopRangingBeaconsInRegion(region)
            stopSelf()
            cubeacon.disconnect(this)
            stopForeground(STOP_FOREGROUND_REMOVE)

            localRepository.updateItemFromQueue(TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy HH:mm"))
            val unsentAttendance = localRepository.getListOfUnsentAttendance()
            remoteRepository.doStoreCurrentAttendance(unsentAttendance!!.map { item -> item.attendanceResponse!! }
            ) { localRepository.releaseAllExecutedItem() }

            val pair = localRepository.getPairSession()
            if(pair.first != null) {
                NotificationManager.showNotification(
                    this,
                    NotificationManager.buildNotification(
                        this, "Anda tercatat hadir",
                            "Tercatat hadir pada sesi ke-${pair.first?.sesi}"
                        ).build()
                )
            }
            if (pair.first != null || pair.second != null) {
                val newIntent = Intent(this, BeaconBackgroundService::class.java)
                newIntent.apply {
                    putExtra("time_execution", TimeUtils.getDiff(startTime, endTime).toString())
                    putExtra("macAddress", macAddress)
                    action = "INIT_RANGING"
                }
                startService(newIntent)
            }
            WakeLocker.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        region = CBRegion("regiontest", UUID.fromString(Constants.REGION))
        realm = Realm.getDefaultInstance()
        localRepository = LocalRepository()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            "INIT_RANGING" -> {
                Log.i("xx", "INIT_RANGING_CALLED")
                macAddress = intent.getStringExtra("macAddress")
                startForeground(
                    5,
                    NotificationManager.buildForegroundNotification(this, intent.getStringExtra("time_execution"))
                )
                val pair = localRepository.getPairSession()
                startTime = Date(System.currentTimeMillis())
                endTime = if (pair.second == null) {
                    TimeUtils.convertStringToDate(pair.first!!.jamSelesai)!!
                } else {
                    TimeUtils.convertStringToDate(pair.second!!.jamMulai)!!
                }
                val newIntent = Intent(this, BeaconBackgroundService::class.java)
                newIntent.action = "START_RANGING"
                newIntent.putExtra("macAddress", intent.getStringExtra("macAddress"))
                val alarmIntent = newIntent.let { intent ->
                    PendingIntent.getService(this, 100, intent, 0)
                }
                val alarmMgr = PermissionManager(this).getAlarmSystemService()
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 60 * 1000,
                    alarmIntent
                )
            }
            "START_RANGING" -> {
                PermissionManager.switchBluetoothState()
                macAddress = intent.getStringExtra("macAddress")
                Log.i("xx", "START_RANGING_CALLED")
                WakeLocker.acquire(this)
                Cubeacon.initialize(this)
                cubeacon = Cubeacon.getInstance()
                cubeacon.connect(this)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cubeacon.removeRangingListener(this)
        cubeacon.disconnect(this)
    }

    override fun onBeaconServiceConnect() {
        Log.i("XX", "ONBEACONSERVICE CALLED")
        Log.i("XX", "MACADDRESS: $macAddress")
        cubeacon.addRangingListener(this)
        cubeacon.startRangingBeaconsInRegion(region)
        handler = Handler()
        handler.postDelayed({
            Log.i("XX", "POST_DELAYED_CALLED")
            PermissionManager.switchBluetoothState()
            cubeacon.stopRangingBeaconsInRegion(region)
            NotificationManager.showNotification(
                this,
                NotificationManager
                    .buildNotification(
                        this,
                        "Beacon tidak ditemukan",
                        "Anda dianggap tidak hadir"
                    ).build()
            )
            val newIntent = Intent(this, BeaconService::class.java)
            newIntent.apply {
                action = "INIT_RANGING"
                putExtra("time_execution", TimeUtils.getDiff(startTime, endTime).toString())
                putExtra("macAddress", macAddress)
            }
            startService(newIntent)
            stopSelf()
            cubeacon.disconnect(this)
            WakeLocker.release()
        }, 25 * 1000)
    }

}