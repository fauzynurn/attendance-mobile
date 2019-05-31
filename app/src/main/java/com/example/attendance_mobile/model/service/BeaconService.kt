package com.example.attendance_mobile.model.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.attendance_mobile.beaconscanning.BeaconScanContract
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.NotificationManager
import com.example.attendance_mobile.utils.TimeUtils
import com.example.attendance_mobile.utils.WakeLocker
import com.eyro.cubeacon.*
import java.util.*

class BeaconService : CBServiceListener,CBRangingListener, Service(){
    private lateinit var cubeacon : Cubeacon
    private lateinit var region : CBRegion
    private lateinit var handler : Handler
    private var macAddress : String = ""

    override fun didRangeBeaconsInRegion(p0: MutableList<CBBeacon>?, p1: CBRegion?) {
        if(p0!!.size > 0 && p0.any { it.macAddress == macAddress }){
            handler.removeCallbacksAndMessages(null)
            cubeacon.stopRangingBeaconsInRegion(region)
            stopSelf()
            cubeacon.disconnect(this)
            stopForeground(STOP_FOREGROUND_REMOVE)
            val intent = Intent()
            intent.putExtra("macAddress",macAddress)
            intent.action = "ON_BEACON_FOUND"
            sendBroadcast(intent)
            WakeLocker.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        region = CBRegion("regiontest", UUID.fromString(Constants.REGION))
        if(intent.action == "INIT_RANGING") {
            startForeground(5, NotificationManager.buildForegroundNotification(this))
            val broadcastIntent = Intent()
            broadcastIntent.action = "START_ALARM"
            sendBroadcast(broadcastIntent)
        }else if(intent.action == "START_RANGING"){
            Cubeacon.initialize(this)
            cubeacon = Cubeacon.getInstance()
            macAddress = intent.getStringExtra("macAddress")
            cubeacon.connect(this)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cubeacon.removeRangingListener(this)
        cubeacon.disconnect(this)
    }

    override fun onBeaconServiceConnect() {
            cubeacon.addRangingListener(this)
            cubeacon.startRangingBeaconsInRegion(region)
            handler = Handler()
            handler.postDelayed({
                cubeacon.stopRangingBeaconsInRegion(region)
                val intent = Intent()
                intent.action = "ON_TIMEOUT"
                sendBroadcast(intent)
                stopSelf()
                cubeacon.disconnect(this)
                WakeLocker.release()
            },25*1000)
    }

    class BeaconReceiver(private val interactorContract: BeaconScanContract.InteractorContract? = null) : BroadcastReceiver(){
        private lateinit var localRepository: LocalRepository
        private lateinit var startTime : Date
        private lateinit var endTime : Date
        override fun onReceive(context: Context, intent: Intent) {
            localRepository = LocalRepository(context)
            when(intent.action){
                "START_ALARM" -> {
                    Log.i("START", "MASUK KE START ALARM")
                    val kodeMatkul = SharedPreferenceHelper(context).getSharedPreferenceString("kodeMatkul","")
                    val newIntent = Intent(context, BeaconService::class.java)
                    localRepository.getPairSession(kodeMatkul!!) { list ->
                        startTime = TimeUtils.convertStringToDate(list[0]?.jamMulai!!)!!
                        endTime = if(list[1] == null){
                            TimeUtils.convertStringToDate(list[0]?.jamSelesai!!)!!
                        }else{
                            TimeUtils.convertStringToDate(list[1]?.jamMulai!!)!!
                        }
                        newIntent.action = "START_RANGING"
                        val alarmIntent = newIntent.let { intent ->
                            PendingIntent.getService(context, 100, intent, 0)
                        }
                        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        alarmMgr.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + TimeUtils.getDiff(startTime,endTime) *60 * 1000,
                            alarmIntent
                        )
                    }
                }
                Constants.ON_BEACON_FOUND -> {
                    val kodeMatkul = SharedPreferenceHelper(context).getSharedPreferenceString("kodeMatkul","")
                    localRepository.releaseSchedule(kodeMatkul!!)
                    localRepository.getListOfUnsentAttendance{
                        Log.i("s","after release:  ${it.size}") }
                    if(interactorContract == null){
                        localRepository.getPairSession(kodeMatkul!!){list ->
                            if(list.isEmpty()){
                                context.unregisterReceiver(this)
                            }else{
                                NotificationManager.showNotification(context,
                                    NotificationManager.buildNotification(context,"Anda tercatat hadir",
                                        "Empty for now").build())
                                val intentFilter = IntentFilter()
                                intentFilter.addAction("ON_BEACON_FOUND")
                                intentFilter.addAction("ON_TIMEOUT")
                                intentFilter.addAction("START_ALARM")
                                context.registerReceiver(this, intentFilter)

                                val newIntent = Intent(context, BeaconService::class.java)
                                newIntent.action = "INIT_RANGING"
                                context.startService(newIntent)
                            }
                        }
                    }else{
                        interactorContract.onDeviceInBeaconRange(intent.getStringExtra("macAddress"))
                        context.unregisterReceiver(this)
                    }
                }
                Constants.ON_TIMEOUT -> {
                    interactorContract?.onBeaconRangingTimeout()
                    context.unregisterReceiver(this)
                    val kodeMatkul = SharedPreferenceHelper(context).getSharedPreferenceString("kodeMatkul","")
                    localRepository.releaseSchedule(kodeMatkul!!)
                    NotificationManager.showNotification(context,
                        NotificationManager
                            .buildNotification(context,"Beacon tidak ditemukan","Waktu pemindaian beacon telah habis").build())
                }
            }
        }
    }
}