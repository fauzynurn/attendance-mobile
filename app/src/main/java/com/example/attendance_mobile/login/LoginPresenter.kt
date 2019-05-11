package com.example.attendance_mobile.login

import com.example.attendance_mobile.data.Response
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.KeyManager
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.Repository
import com.example.attendance_mobile.utils.Constants

class LoginPresenter(private val view: LoginContract.ViewContract,
                     private val repository : Repository,
                     private val keyMgr : KeyManager,
                     private val sharedPreferenceHelper: SharedPreferenceHelper,
                     private val permissionManager: PermissionManager) : LoginContract.InteractorContract{
    var nimTemp : String = "0"
    var kddsnTemp : String = "0"

    override fun onFail(error: String?) {
        view.showSnackBar("Error ocurred, $error")
    }

    fun onEnterApp(){
        val status = sharedPreferenceHelper.getSharedPreferenceInt("status",-1)
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

    override fun onValidateMhsResult(response: Response?) {
        val imei : String
        val pubKey: String
        when(response?.message){
            Constants.NOT_YET_ACTIVE_STATUS -> {
                if(permissionManager.isPermissionGranted("android.permission.READ_PHONE_STATE")){
                    imei = permissionManager.getImei()
                    pubKey = keyMgr.generateKeyPair()
                    repository.doRegisterMhs(pubKey,nimTemp,imei,this)
                }else{
                    view.showSnackBar(Constants.PERMISSION_DENIED)
                }
            }
            Constants.IMEI_MATCH -> {
                view.startHomeMhs()
            }
            Constants.STILL_ACTIVE_STATUS->{
                view.showDialog(Constants.STILL_ACTIVE_MESSAGE)
            }
            else -> {
                view.showSnackBar(Constants.NOT_VERIFIED_MESSAGE)
            }
        }
    }

    override fun onValidateDsnResult(response: Response?) {
        val imei : String
        val pubKey: String
        when(response?.message){
            Constants.NOT_YET_ACTIVE_STATUS -> {
                if(permissionManager.isPermissionGranted("android.permission.READ_PHONE_STATE")){
                    imei = permissionManager.getImei()
                    pubKey = keyMgr.generateKeyPair()
                    repository.doRegisterDsn(pubKey,kddsnTemp,imei,this)
                }else{
                    view.showSnackBar(Constants.PERMISSION_DENIED)
                }
            }
            Constants.IMEI_MATCH -> {
                view.startHomeDsn()
            }
            Constants.STILL_ACTIVE_STATUS->{
                view.showDialog(Constants.STILL_ACTIVE_MESSAGE)
            }
            else -> {
                view.showSnackBar(Constants.NOT_VERIFIED_MESSAGE)
            }
        }
    }

    override fun onRegisterMhsResult(response: Response?) {
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
                nimTemp = nim
                repository.doValidateMhs(nim,pass,permissionManager.getImei(),this)
            } else {
                view.showSnackBar(Constants.MHS_INVALID_FORM)
            }
        }else {
            if(kddosen != "" && pass != ""){
                kddsnTemp = kddosen
                repository.doValidateDosen(kddosen,pass,permissionManager.getImei(),this)
            }else{
                view.showSnackBar(Constants.DOSEN_INVALID_FORM)
            }
        }
    }

}