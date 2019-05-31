package com.example.attendance_mobile.model.manager

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import com.example.attendance_mobile.utils.Base64
import java.security.*

class KeyManager{
        fun generateKeyPair(): String {
            try {
                val mKeyPairGenerator: KeyPairGenerator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
                mKeyPairGenerator.initialize(
                    KeyGenParameterSpec.Builder(
                        "ALIAS",
                        KeyProperties.PURPOSE_SIGN
                    )
                        .setKeySize(512)
                        .setDigests(KeyProperties.DIGEST_SHA512, KeyProperties.DIGEST_SHA1)
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                        .setUserAuthenticationRequired(true)
                        .build()
                )
                val keyPair: KeyPair = mKeyPairGenerator.generateKeyPair()
                return Base64.encodeToString(keyPair.public.encoded, 0)
            } catch (e: InvalidAlgorithmParameterException) {
                throw RuntimeException(e)
            }
        }

    companion object {
        fun getSignatureObject(): Signature? {
            try {
                val mKeyStore = KeyStore.getInstance("AndroidKeyStore")
                val mSignature = Signature.getInstance("SHA1WithRSA")
                mKeyStore!!.load(null)
                val key = mKeyStore.getKey("ALIAS", null) as PrivateKey
                mSignature!!.initSign(key)
                return mSignature
            }catch (e: KeyPermanentlyInvalidatedException) {
                return null
            }catch(e : Exception){
                throw RuntimeException("Failed to init Cipher", e)
            }
        }
    }
}