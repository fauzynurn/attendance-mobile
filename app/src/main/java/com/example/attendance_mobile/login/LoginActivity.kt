package com.example.attendance_mobile.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.HomeDsnActivity
import com.example.attendance_mobile.home.homemhs.HomeMhsActivity
import com.example.attendance_mobile.model.local.SharedPreferenceHelper
import com.example.attendance_mobile.model.manager.KeyManager
import com.example.attendance_mobile.model.manager.PermissionManager
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.utils.Constants
import com.example.attendance_mobile.utils.NotificationManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity(), LoginContract.ViewContract {
    override lateinit var presenter: LoginPresenter
    private var currentTab : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = LoginPresenter(this, RemoteRepository(),
            KeyManager(),
            SharedPreferenceHelper(this),
            PermissionManager(this)
        )
        presenter.onEnterApp()
    }

    override fun toggleBtn() {
        next_btn.isEnabled = !next_btn.isEnabled
    }

    override fun startHomeMhs() {
        startActivity(Intent(this,HomeMhsActivity::class.java))
        finish()
    }

    override fun startHomeDsn() {
        startActivity(Intent(this, HomeDsnActivity::class.java))
        finish()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(parent_login,message,Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog(title : String, message: String, isWithExitListener : Boolean) {
        MaterialAlertDialogBuilder(this,R.style.DialogTheme).run {
            setTitle(title)
            setMessage(message)
            if(isWithExitListener) setPositiveButton("Ok") { _,_ -> finishAffinity()
            } else setPositiveButton("Ok",null)
            show()
        }
    }

    private fun handleSubTitle(){
        if(currentTab == 1) subtitle.text = Constants.MAHASISWA_SUBTITLE else subtitle.text = Constants.DOSEN_SUBTITLE
    }

    override fun onFirstTimeUse() {
        requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE),0)
        NotificationManager.createNotificationChannel(this,PermissionManager(this).getNotificationSystemService())
        mhs_tab.isChecked = true
        mhs_tab.setOnClickListener {
            currentTab = 1
            handleSubTitle()
            mhs_tab.isChecked = true
            dosen_tab.isChecked = false
            nim_field.visibility = View.VISIBLE
            kddsn_field.apply {
                visibility = View.INVISIBLE
                editText?.text?.clear()
            }
        }
        dosen_tab.setOnClickListener {
            currentTab = 2
            handleSubTitle()
            dosen_tab.isChecked = true
            mhs_tab.isChecked = false
            kddsn_field.visibility = View.VISIBLE
            nim_field.apply {
                visibility = View.INVISIBLE
                editText?.text?.clear()
            }
        }
        next_btn.setOnClickListener {
            val nimText : String  = nim_field.editText?.text.toString()
            val kddsnText : String  = kddsn_field.editText?.text.toString()
            presenter.handleLogin(currentTab,kddsnText,nimText)
        }
    }

}