package com.example.attendance_mobile.model.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.attendance_mobile.data.KehadiranPerSesi
import com.example.attendance_mobile.data.request.AttendanceRequest
import com.example.attendance_mobile.data.request.ListAttendanceRequest
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.NotificationManager
import com.example.attendance_mobile.utils.TimeUtils
import com.example.attendance_mobile.utils.WakeLocker
import com.eyro.cubeacon.*
import java.util.*

class BeaconBackgroundService : CBServiceListener, CBRangingListener, Service() {
    private lateinit var cubeacon: Cubeacon
    private lateinit var region: CBRegion
    private lateinit var handler: Handler
    private var macAddress: String = ""
    private lateinit var localRepository: LocalRepository
    private lateinit var remoteRepository : RemoteRepository
    private lateinit var startTime: Date
    private var session = ""
    private lateinit var endTime: Date
    private lateinit var pair : Pair<KehadiranPerSesi?, KehadiranPerSesi?>
    override fun didRangeBeaconsInRegion(p0: MutableList<CBBeacon>?, p1: CBRegion?) {
        if (p0!!.size > 0 && p0.any { it.macAddress == macAddress }) {
            PermissionManager.switchBluetoothState()
            Log.i("XX", "RANGE_IN_REGION_CALLED")
            handler.removeCallbacksAndMessages(null)
            cubeacon.stopRangingBeaconsInRegion(region)
            stopSelf()
            cubeacon.disconnect(this)
            stopForeground(STOP_FOREGROUND_REMOVE)
            //expected behaviour : sesi 5 dan 6
            pair = localRepository.getPairSession()
            // update sesi 5
            localRepository.updateItemFromQueue()
            val unsentAttendance = localRepository.getListOfUnsentAttendance()
            val nim = SharedPreferenceHelper(this).getSharedPreferenceString("nim", "")!!
            val list = unsentAttendance!!.map { item ->
                AttendanceRequest(
                    nim,
                    item.sesi.toString(),
                    TimeUtils.getDateInString(TimeUtils.getCurrentDate(), TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"))
                )
            }
            remoteRepository.doStoreCurrentAttendance(
                ListAttendanceRequest(list)
            ) {
                //expected behaviour : notif sukses UNTUK sesi 5
                NotificationManager.showNotification(
                    this,
                    NotificationManager.buildNotification(
                        this, "Anda tercatat hadir",
                        "Tercatat hadir pada sesi ke-$session"
                    ).build()
                )
                localRepository.releaseAllExecutedItem()
            }

            //update data pair setelah releaseAllExecutedItem() dieksekusi
            //ada dua kondisi : muncul satu (dlm kasus ini, ada satu), muncul dua ( kalo ada 4 sesi )
            pair = localRepository.getPairSession()
            if (pair.first != null || pair.second != null) {
                val newIntent = Intent(this, BeaconBackgroundService::class.java)
                newIntent.apply {
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
        localRepository = LocalRepository()
        remoteRepository = RemoteRepository()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            "INIT_RANGING" -> {
                Log.i("xx", "INIT_RANGING_CALLED")
//                macAddress = intent.getStringExtra("macAddress")
                //isi pair: sesi 5 dan 6
                pair = localRepository.getPairSession()
                startTime = Date(System.currentTimeMillis())
                endTime = TimeUtils.convertStringToDate(pair.first!!.jamMulai)!!
                startForeground(
                    5,
                    //time execution disini adalah waktu dimana pemindaian beacon untuk sesi 5 akan dilakukan
                    NotificationManager.buildForegroundNotification(this, TimeUtils.getDiff(startTime,endTime).toString())
                )
                // jangan di stop. entar notif foreground nya mati
                val newIntent = Intent(this, BeaconBackgroundService::class.java)
                newIntent.action = "START_RANGING"
                newIntent.putExtra("macAddress", intent.getStringExtra("macAddress"))
                session = pair.first?.sesi.toString()
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
                Handler().postDelayed({
                    onWithoutBeacon()
                },5000)
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

    fun onWithoutBeacon(){
        PermissionManager.switchBluetoothState()
        Log.i("XX", "RANGE_IN_REGION_CALLED")
        handler.removeCallbacksAndMessages(null)
        cubeacon.stopRangingBeaconsInRegion(region)
        stopSelf()
        cubeacon.disconnect(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        //expected behaviour : sesi 5 dan 6
        pair = localRepository.getPairSession()
        // update sesi 5
        localRepository.updateItemFromQueue()
        val unsentAttendance = localRepository.getListOfUnsentAttendance()
        val nim = SharedPreferenceHelper(this).getSharedPreferenceString("nim", "")!!
        val list = unsentAttendance!!.map { item ->
            AttendanceRequest(
                nim,
                item.sesi.toString(),
                TimeUtils.getDateInString(TimeUtils.getCurrentDate(), TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"))
            )
        }
        remoteRepository.doStoreCurrentAttendance(
            ListAttendanceRequest(list)
        ) {
            //expected behaviour : notif sukses UNTUK sesi 5
            NotificationManager.showNotification(
                this,
                NotificationManager.buildNotification(
                    this, "Anda tercatat hadir",
                    "Tercatat hadir pada sesi ke-$session"
                ).build()
            )
            localRepository.releaseAllExecutedItem()
        }

        //update data pair setelah releaseAllExecutedItem() dieksekusi
        //ada dua kondisi : muncul satu (dlm kasus ini, ada satu), muncul dua ( kalo ada 4 sesi )
        pair = localRepository.getPairSession()
        if (pair.first != null || pair.second != null) {
            val newIntent = Intent(this, BeaconBackgroundService::class.java)
            newIntent.apply {
                putExtra("macAddress", macAddress)
                action = "INIT_RANGING"
            }
            startService(newIntent)
        }
        WakeLocker.release()
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
            //keep rescheduling alarm
            val newIntent = Intent(this, BeaconBackgroundService::class.java)
            newIntent.action = "START_RANGING"
            newIntent.putExtra("macAddress", macAddress)
            session = pair.first?.sesi.toString()
            val alarmIntent = newIntent.let { intent ->
                PendingIntent.getService(this, 100, intent, 0)
            }
            val alarmMgr = PermissionManager(this).getAlarmSystemService()
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 60 * 1000,
                alarmIntent
            )
            cubeacon.disconnect(this)
            WakeLocker.release()
        }, 5 * 60 * 1000)
    }

}