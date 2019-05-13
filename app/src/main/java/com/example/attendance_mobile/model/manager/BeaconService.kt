package com.example.attendance_mobile.model.manager

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.attendance_mobile.beaconscanning.BeaconScanContract
import com.example.attendance_mobile.utils.Constants
import com.eyro.cubeacon.*
import java.util.*

class BeaconService : CBServiceListener,CBRangingListener, Service(){
    private lateinit var cubeacon : Cubeacon
    private lateinit var region : CBRegion
    private var macAddress : String = ""
    override fun didRangeBeaconsInRegion(p0: MutableList<CBBeacon>?, p1: CBRegion?) {
        if(p0!!.any { it.macAddress == macAddress }){
            val intent = Intent()
            intent.action = Constants.START_RANGING
            sendBroadcast(intent)
            cubeacon.stopRangingBeaconsInRegion(region)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Cubeacon.initialize(this)
        cubeacon = Cubeacon.getInstance()
        macAddress = intent.getStringExtra("macAddress")
        cubeacon.connect(this)
        region = CBRegion(
            "regiontest", UUID.fromString(Constants.REGION)
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cubeacon.removeRangingListener(this)
    }

    override fun onBeaconServiceConnect() {
        try {
            cubeacon.addRangingListener(this)
            cubeacon.startRangingBeaconsInRegion(region)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Constants.ON_BEACON_ERROR
            intent.putExtra("exception",e.message)
            sendBroadcast(intent)
        }
    }

    class BeaconReceiver(private val interactorContract: BeaconScanContract.InteractorContract) : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.action == Constants.START_RANGING){
                interactorContract.onDeviceInBeaconRange()
            }else{
                interactorContract.onBeaconError(intent.getStringExtra("exception"))
            }
        }
    }
}