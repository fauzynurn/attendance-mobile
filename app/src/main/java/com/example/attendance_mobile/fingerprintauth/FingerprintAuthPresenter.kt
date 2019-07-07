package com.example.attendance_mobile.fingerprintauth

import com.example.attendance_mobile.data.request.AttendanceRequest
import com.example.attendance_mobile.data.request.ListAttendanceRequest
import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.FingerprintHandler
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils

class FingerprintAuthPresenter(
    val remoteRepository: RemoteRepository,
    val localRepository: LocalRepository,
    val view: FingerprintAuthContract.ViewContract,
    val sharedPreferenceHelper: SharedPreferenceHelper,
    val permissionManager: PermissionManager
) : FingerprintAuthContract.InteractorContract {
    var macAddress: String = ""
    fun startAuth(macAddress: String) {
        this.macAddress = macAddress
        val fingerprintHandler = FingerprintHandler(this)
        fingerprintHandler.startFingerprintService(permissionManager.getFingerprintSystemService())
    }

    override fun onAuthenticated() {
        localRepository.updateItemFromQueue()
        val list = localRepository.getListOfUnsentAttendance()
        val nim = sharedPreferenceHelper.getSharedPreferenceString("nim", "")
        val attList = list?.map { item ->
            AttendanceRequest(nim!!, item.sesi.toString(), TimeUtils.getDateInString(TimeUtils.getCurrentDate(), "dd-MM-yyyy"))
        }

        remoteRepository.doStoreCurrentAttendance(
            ListAttendanceRequest(
                attList!!
            )
        ) {
            localRepository.releaseAllExecutedItem()
        }
        val newlist = localRepository.getListOfUnsentAttendance()
        view.startHome()
        view.setAlarm(permissionManager.getAlarmSystemService(), macAddress)
    }

    override fun onAuthenticationFail() {
        view.showToast(Constants.FINGERPRINT_NOT_RECOGNIZED_MESSAGE)
        localRepository.deleteAllRows()
    }

    override fun onAuthenticationHelp(message: String) {
        view.showToast(message)
        localRepository.deleteAllRows()
    }

    override fun onAuthenticationError(message: String) {
        view.showDialog("Kesalahan teknis terjadi", message)
        localRepository.deleteAllRows()
    }

}