package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.model.service.BeaconService

interface BeaconScanContract {
    interface ViewContract : BaseView<BeaconScanPresenter>{
        fun registerReceiver(beaconReceiver: BeaconService.BeaconReceiver)
        fun unregisterReceiver(beaconReceiver: BeaconService.BeaconReceiver)
        fun stopService()
        fun startHome()
        fun startService(macAddress: String)
        fun startFingerprintActivity(macAddress: String)
        fun showDialog(title : String, message: String)
        fun startHomeDsn()
        fun startHomeMhs()
    }

    interface InteractorContract{
        fun onBeaconRangingTimeout()
        fun onDeviceInBeaconRange(macAddress: String)
        fun onBeaconError(message : String)
    }
}