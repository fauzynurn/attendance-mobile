package com.example.attendance_mobile.fingerprintauth

import com.example.attendance_mobile.model.local.LocalRepository
import com.example.attendance_mobile.model.manager.FingerprintHandler
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.TimeUtils
import java.util.*

class FingerprintAuthPresenter(val localRepository: LocalRepository,val view : FingerprintAuthContract.ViewContract, val permissionManager: PermissionManager) : FingerprintAuthContract.InteractorContract{
    var macAddress : String = ""
    fun startAuth(macAddress : String){
        this.macAddress = macAddress
        val fingerprintHandler = FingerprintHandler(this)
        fingerprintHandler.startFingerprintService(permissionManager.getFingerprintSystemService())
    }
    override fun onAuthenticated() {
        val list = localRepository.getListOfUnsentAttendance()
        val pair = localRepository.getPairSession(false)
        val startTime = Date(System.currentTimeMillis())
        val endTime = if (pair.second == null) {
            TimeUtils.convertStringToDate(pair.first!!.jamSelesai)!!
        } else {
            TimeUtils.convertStringToDate(pair.second!!.jamMulai)!!
        }
        view.startHome()
        view.setAlarm(permissionManager.getAlarmSystemService(),macAddress, TimeUtils.getDiff(startTime, endTime))
    }

    override fun onAuthenticationFail() {
        view.showToast(Constants.FINGERPRINT_NOT_RECOGNIZED_MESSAGE)
    }

    override fun onAuthenticationHelp(message: String) {
        view.showToast(message)
    }

    override fun onAuthenticationError(message: String) {
        view.showDialog("Kesalahan teknis terjadi", message)
    }

}