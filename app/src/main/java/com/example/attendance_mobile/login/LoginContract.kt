package com.example.attendance_mobile.login

import android.app.Activity
import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.data.Response

interface LoginContract{
    interface ViewContract : BaseView<LoginPresenter>{
        fun onRegisterMhsSuccess()
        fun onRegisterDsnSuccess()
        fun showSnackBar(message: String)
        fun showDialog(message: String)
    }
    interface InteractorContract{
        fun onValidateMhsResult(response: Response?)
        fun onValidateDsnResult(response: Response?)
        fun onRegisterMhsResult(response: Response?)
        fun onRegisterDsnResult(response: Response?)
        fun onFail(error : String?)
    }
}