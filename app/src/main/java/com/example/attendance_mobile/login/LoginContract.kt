package com.example.attendance_mobile.login

import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.response.BaseResponse

interface LoginContract{
    interface ViewContract : BaseView<LoginPresenter>{
        fun startHomeMhs()
        fun toggleBtn()
        fun startHomeDsn()
        fun onFirstTimeUse()
        fun showSnackBar(message: String)
        fun showDialog(title : String, message: String, isWithExitListener : Boolean)
    }
    interface InteractorContract{
        fun onValidateMhsResult(response: BaseResponse?)
        fun onValidateDsnResult(response: BaseResponse?)
        fun onRegisterMhsResult(response: BaseResponse?)
        fun onRegisterDsnResult()
        fun onFail(error : String?)
    }
}