package com.example.attendance_mobile.beaconscanning

import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils

class BeaconScanPresenter(
    val remoteRepository: RemoteRepository,
    val localRepository: LocalRepository,
    val sharedPreferenceHelper: SharedPreferenceHelper,
    private val view: BeaconScanContract.ViewContract
) : BeaconScanContract.InteractorContract {
    lateinit var receiver: BeaconService.BeaconReceiver<BeaconScanContract.InteractorContract>
    lateinit var macAddress: String
    fun startBeaconRanging(macAddress: String) {
        this.macAddress = macAddress
        receiver = BeaconService.BeaconReceiver(this)
        sharedPreferenceHelper.setSharedPreferenceString("macAddress", macAddress)
        view.registerReceiver(receiver)
        view.startService(macAddress)
    }

    override fun onBeaconFound() {
        view.startFingerprintActivity(macAddress)
        localRepository.updateItemFromQueue(TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy HH:mm"))
        val unsentAttendance = localRepository.getListOfUnsentAttendance()
        remoteRepository.doStoreCurrentAttendance(unsentAttendance!!.map { item -> item.attendanceResponse!! }
        ) { localRepository.releaseAllExecutedItem() }
        PermissionManager.switchBluetoothState()
        view.stopService()
    }

    override fun onBeaconError(message: String) {
        view.showDialog("Beacon ranging tidak dapat dilakukan", message)
    }

    override fun onTimeout() {
        onBackPressed()
    }

    fun onBackPressed() {
        if (sharedPreferenceHelper.getSharedPreferenceInt("status", -1) == Constants.MAHASISWA) {
            view.startHomeMhs()
        } else if (sharedPreferenceHelper.getSharedPreferenceInt("status", -1) == Constants.DOSEN) {
            view.startHomeDsn()
        }
    }
}