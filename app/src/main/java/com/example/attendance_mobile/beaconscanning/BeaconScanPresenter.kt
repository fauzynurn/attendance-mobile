package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.service.BeaconService

class BeaconScanPresenter(
    private val view: BeaconScanContract.ViewContract
) : BeaconScanContract.InteractorContract {
    lateinit var receiver: BeaconService.BeaconReceiver<BeaconScanContract.InteractorContract>
    lateinit var macAddress: String
    fun startBeaconRanging(macAddress: String) {
        this.macAddress = macAddress
        receiver = BeaconService.BeaconReceiver(this)
        view.registerReceiver(receiver)
        view.startService(macAddress)
    }

    override fun onBeaconFound() {
        view.startFingerprintActivity(macAddress)
//        localRepository.updateItemFromQueue()
//        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")!!
//        val unsentAttendance = localRepository.getListOfUnsentAttendance()
//        remoteRepository.doStoreCurrentAttendance(unsentAttendance!!.map { item ->
//            AttendanceRequest(
//                nim,
//                item.kodeMatkul,
//                TimeUtils.getDateInString(TimeUtils.getCurrentDate(), item.tglKuliah)
//            )
//        }
//        ) { localRepository.releaseAllExecutedItem() }
        PermissionManager.switchBluetoothState()
        view.stopService()
    }

    override fun onTimeout() {
        onBackPressed()
    }

    fun onBackPressed() {
        view.startHomeMhs()
    }
}