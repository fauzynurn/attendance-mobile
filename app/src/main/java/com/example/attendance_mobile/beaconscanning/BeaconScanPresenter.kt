package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.model.manager.BeaconService

class BeaconScanPresenter(private val view : BeaconScanContract.ViewContract) : BeaconScanContract.InteractorContract {
    fun startBeaconRanging(macAddress : String){
        if(view.checkIfBluetoothActive()) {
            view.registerReceiver(BeaconService.BeaconReceiver(this))
            view.startService(macAddress)
        }else{
            view.showDialog("Bluetooth tidak aktif","Silahkan aktifkan dahulu bluetooth anda")
        }
    }

    override fun onDeviceInBeaconRange() {
        view.startFingerprintActivity()
    }

    override fun onBeaconError(message: String) {
        view.showDialog("Beacon ranging tidak dapat dilakukan", message)
    }
}