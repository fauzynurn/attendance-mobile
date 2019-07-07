package com.example.attendance_mobile.model.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.attendance_mobile.model.BaseBeaconInteractor
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.NotificationManager
import com.example.attendance_mobile.utils.WakeLocker
import com.eyro.cubeacon.*
import java.util.*

//Coba receiver nya dijadiin variabel ( bukan class ). jadi harapannya bisa di start atau di stop sm service.
//pertimbangin juga buat beacon ranging di luar kelas ini ( fingerprintauth ) bakal  kompatibel ga
class BeaconService : CBServiceListener, CBRangingListener, Service() {
    private lateinit var cubeacon: Cubeacon
    private lateinit var region: CBRegion
    private lateinit var handler: Handler
    private var macAddress: String = ""

    override fun didRangeBeaconsInRegion(p0: MutableList<CBBeacon>?, p1: CBRegion?) {
        if (p0!!.size > 0 && p0.any { it.macAddress == macAddress }) {
            Log.i("XX", "RANGE_IN_REGION_CALLED")
            handler.removeCallbacksAndMessages(null)
            cubeacon.stopRangingBeaconsInRegion(region)
            stopSelf()
            cubeacon.disconnect(this)
            val intent = Intent()
            intent.putExtra("macAddress", macAddress)
            intent.action = "ON_BEACON_FOUND"
            sendBroadcast(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        region = CBRegion("regiontest", UUID.fromString(Constants.REGION))
        macAddress = intent.getStringExtra("macAddress")
        Log.i("xx", "START_RANGING_CALLED")
        WakeLocker.acquire(this)
        Handler().postDelayed({
            val intent = Intent()
            intent.putExtra("macAddress", macAddress)
            intent.action = "ON_BEACON_FOUND"
            sendBroadcast(intent)
        },5000)
        Cubeacon.initialize(this)
        cubeacon = Cubeacon.getInstance()
        cubeacon.connect(this)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cubeacon.removeRangingListener(this)
        cubeacon.disconnect(this)
    }

    override fun onBeaconServiceConnect() {
        Log.i("XX", "ONBEACONSERVICE CALLED")
        cubeacon.addRangingListener(this)
        cubeacon.startRangingBeaconsInRegion(region)
        handler = Handler()
        handler.postDelayed({
            Log.i("XX", "POST_DELAYED_CALLED")
            cubeacon.stopRangingBeaconsInRegion(region)
            val intent = Intent()
            intent.action = "ON_TIMEOUT"
            sendBroadcast(intent)
            stopSelf()
            cubeacon.disconnect(this)
            WakeLocker.release()
        }, 25 * 1000)
    }

    class BeaconReceiver<T : BaseBeaconInteractor>(private val interactorContract: T) :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Constants.ON_BEACON_FOUND -> {
                    Log.i("xx", "ON_BEACON_FOUND_CALLED")
                    interactorContract.onBeaconFound()
                }
                Constants.ON_TIMEOUT -> {
                    Log.i("xx", "ON_TIMEOUT_CALLED")
                    val localRepository = LocalRepository()
                    localRepository.deleteAllRows()
                    interactorContract.onTimeout()
                    NotificationManager.showNotification(
                        context,
                        NotificationManager
                            .buildNotification(
                                context,
                                "Beacon tidak ditemukan",
                                "Silahkan coba kembali"
                            ).build()
                    )
                }
            }
            context.unregisterReceiver(this)
        }
    }
}