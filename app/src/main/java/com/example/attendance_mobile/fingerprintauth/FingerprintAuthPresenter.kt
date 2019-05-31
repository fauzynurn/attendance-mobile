package com.example.attendance_mobile.fingerprintauth

import com.example.attendance_mobile.model.manager.FingerprintHandler
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.utils.Constants

class FingerprintAuthPresenter(val view : FingerprintAuthContract.ViewContract, val permissionManager: PermissionManager) : FingerprintAuthContract.InteractorContract{
    var macAddress : String = ""
    fun startAuth(macAddress : String){
        this.macAddress = macAddress
        val fingerprintHandler = FingerprintHandler(this)
        fingerprintHandler.startFingerprintService(permissionManager.getFingerprintSystemService())
    }
    override fun onAuthenticated() {
        view.startHome()
//        view.setAlarm(permissionManager.getAlarmSystemService())
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