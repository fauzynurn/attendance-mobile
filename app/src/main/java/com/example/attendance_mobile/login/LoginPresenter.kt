package com.example.attendance_mobile.login

import com.example.attendance_mobile.data.response.BaseResponse
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.KeyManager
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants

class LoginPresenter(private val view: LoginContract.ViewContract,
                     private val remoteRepository : RemoteRepository,
                     private val keyMgr : KeyManager,
                     private val sharedPreferenceHelper: SharedPreferenceHelper,
                     private val permissionManager: PermissionManager) : LoginContract.InteractorContract{
    var nimTemp : String = "0"
    var kddsnTemp : String = "0"

    override fun onFail(error: String?) {
        view.toggleBtn()
        view.showSnackBar("Error ocurred, $error")
    }

    fun onEnterApp(){
        val status = sharedPreferenceHelper.getSharedPreferenceInt("status",-1)
        if(!permissionManager.checkIfFingerprintScanSupported()){
            view.showDialog(Constants.FINGERPRINT_NOT_SUPPORTED_TITLE,Constants.FINGERPRINT_NOT_SUPPORTED_MESSAGE,true)
        }else if(!permissionManager.checkIfHasFingerprintEnrolled()){
            view.showDialog(Constants.FINGERPRINT_DATA_NOT_FOUND_TITLE,Constants.FINGERPRINT_DATA_NOT_FOUND_MESSAGE,true)
        }
        if(status != -1){
            if(status == Constants.MAHASISWA){
                view.startHomeMhs()
            }else{
                view.startHomeDsn()
            }
        }else{
            view.onFirstTimeUse()
        }
    }

    override fun onValidateMhsResult(response: BaseResponse?) {
        val imei : String
        val pubKey: String
        when(response?.message){
            Constants.NOT_YET_ACTIVE_STATUS -> {
                if(permissionManager.isPermissionGranted("android.permission.READ_PHONE_STATE")){
                    imei = permissionManager.getImei()
                    pubKey = keyMgr.generateKeyPair()
                    remoteRepository.doRegisterMhs(pubKey,nimTemp,imei,this)
                }else{
                    view.showSnackBar(Constants.PERMISSION_DENIED)
                }
            }
            Constants.IMEI_MATCH -> {
                sharedPreferenceHelper.apply {
                    setSharedPreferenceString("nim",nimTemp)
                    setSharedPreferenceInt("status",Constants.MAHASISWA)
                    setSharedPreferenceString("kelas",response.data)
                }
                view.startHomeMhs()
            }
            Constants.STILL_ACTIVE_STATUS->{
                view.showDialog(Constants.LOGIN_ERROR, Constants.STILL_ACTIVE_MESSAGE, false)
            }
            else -> {
                view.showSnackBar(Constants.NOT_VERIFIED_MESSAGE)
            }
        }
    }

    override fun onValidateDsnResult(response: BaseResponse?) {
        val imei : String
        val pubKey: String
        when(response?.message){
            Constants.NOT_YET_ACTIVE_STATUS -> {
                if(permissionManager.isPermissionGranted("android.permission.READ_PHONE_STATE")){
                    imei = permissionManager.getImei()
                    pubKey = keyMgr.generateKeyPair()
                    remoteRepository.doRegisterDsn(pubKey,kddsnTemp,imei,this)
                }else{
                    view.showSnackBar(Constants.PERMISSION_DENIED)
                }
            }
            Constants.IMEI_MATCH -> {
                view.startHomeDsn()
            }
            Constants.STILL_ACTIVE_STATUS->{
                view.showDialog(Constants.LOGIN_ERROR, Constants.STILL_ACTIVE_MESSAGE, false)
            }
            else -> {
                view.showSnackBar(Constants.NOT_VERIFIED_MESSAGE)
            }
        }
    }

    override fun onRegisterMhsResult(response: BaseResponse?) {
        sharedPreferenceHelper.apply {
            setSharedPreferenceString("nim",nimTemp)
            setSharedPreferenceInt("status",Constants.MAHASISWA)
            setSharedPreferenceString("kelas",response!!.message)
        }
        view.startHomeMhs()
    }

    override fun onRegisterDsnResult() {
        sharedPreferenceHelper.apply {
            setSharedPreferenceString("kddsn",kddsnTemp)
            setSharedPreferenceInt("status",Constants.DOSEN)
        }
        view.startHomeDsn()
    }


    fun handleLogin(currentTab : Int , kddosen : String, nim : String, pass : String){
        if(currentTab == 1) {
            if (nim != "" && pass != "") {
                view.toggleBtn()
                nimTemp = nim
                remoteRepository.doValidateMhs(nim,pass,permissionManager.getImei(),this)
            } else {
                view.showSnackBar(Constants.MHS_INVALID_FORM)
            }
        }else {
            if(kddosen != "" && pass != ""){
                view.toggleBtn()
                kddsnTemp = kddosen
                remoteRepository.doValidateDosen(kddosen,pass,permissionManager.getImei(),this)
            }else{
                view.showSnackBar(Constants.DOSEN_INVALID_FORM)
            }
        }
    }

}