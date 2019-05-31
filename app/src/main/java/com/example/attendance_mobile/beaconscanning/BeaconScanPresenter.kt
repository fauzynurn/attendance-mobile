package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.Constants

class BeaconScanPresenter(val sharedPreferenceHelper: SharedPreferenceHelper, private val view : BeaconScanContract.ViewContract) : BeaconScanContract.InteractorContract {
    lateinit var receiver : BeaconService.BeaconReceiver
    fun startBeaconRanging(macAddress : String){
        receiver = BeaconService.BeaconReceiver(this)
        sharedPreferenceHelper.setSharedPreferenceString("macAddress", macAddress)
        view.registerReceiver(receiver)
        view.startService(macAddress)
    }

    override fun onDeviceInBeaconRange(macAddress: String) {
        view.startFingerprintActivity(macAddress)
        PermissionManager.switchBluetoothState()
        view.stopService()
    }

    override fun onBeaconError(message: String) {
        view.showDialog("Beacon ranging tidak dapat dilakukan", message)
    }

    override fun onBeaconRangingTimeout() {
        onBackPressed()
    }

    fun onBackPressed(){
        if(sharedPreferenceHelper.getSharedPreferenceInt("status",-1) == Constants.MAHASISWA){
            view.startHomeMhs()
        }else if(sharedPreferenceHelper.getSharedPreferenceInt("status",-1) == Constants.DOSEN){
            view.startHomeDsn()
        }
    }
}