package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.model.manager.BeaconService

interface BeaconScanContract {
    interface ViewContract : BaseView<BeaconScanPresenter>{
        fun registerReceiver(beaconReceiver: BeaconService.BeaconReceiver)
        fun startService(macAddress : String)
        fun startFingerprintActivity()
        fun showDialog(title : String, message: String)
        fun checkIfBluetoothActive() : Boolean
    }

    interface InteractorContract{
        fun onDeviceInBeaconRange()
        fun onBeaconError(message : String)
    }
}