package com.example.attendance_mobile.model.manager

import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import com.example.attendance_mobile.fingerprintauth.FingerprintAuthContract

class FingerprintHandler(private val interactor: FingerprintAuthContract.InteractorContract) : FingerprintManager.AuthenticationCallback(){
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        interactor.onAuthenticationError(errString.toString())
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        interactor.onAuthenticated()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        interactor.onAuthenticationHelp(helpString.toString())
    }

    override fun onAuthenticationFailed() {
        interactor.onAuthenticationFail()
    }

    fun startFingerprintService(fingerprintManager: FingerprintManager){
        val signatureObject = KeyManager.getSignatureObject()
        if(signatureObject == null){
            interactor
        }
        val cryptoObject = FingerprintManager.CryptoObject(signatureObject)
        fingerprintManager.authenticate(cryptoObject, CancellationSignal(),0,this,null)
    }
}