package com.example.attendance_mobile.model.remote

import com.example.attendance_mobile.data.Response
import com.example.attendance_mobile.login.LoginContract
import com.example.fingerprintauth.RetrofitClient
import com.example.fingerprintauth.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback

class Repository {
    private var retrofitService : RetrofitInterface? = null
    init {
        retrofitService = RetrofitClient.getInstance()?.create(RetrofitInterface::class.java)
    }
    fun doValidateDosen(kddsn : String, pass : String, listener : LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("kddsn",kddsn)
            put("password",pass)
        }
        val call : Call<Response> = retrofitService!!.checkDsn(body)
        call.enqueue(object: Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                listener.onValidateDsnResult(response.body())
            }

        })

    }

    fun doValidateMhs(nim : String, pass : String, listener: LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("nim",nim)
            put("password",pass)
        }
        val call : Call<Response> = retrofitService!!.checkMhs(body)
        call.enqueue(object: Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                listener.onValidateMhsResult(response.body())
            }

        })
    }

    fun doRegisterMhs(publicKey : String, nim : String, imei : String, listener: LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("publicKey",publicKey)
            put("nim",nim)
            put("imei",imei)
        }
        val call : Call<Response> = retrofitService!!.registerMhs(body)
        call.enqueue(object: Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                listener.onRegisterMhsResult(response.body())
            }

        })
    }

    fun doRegisterDsn(publicKey : String, kddsn : String, imei : String, listener: LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("publicKey",publicKey)
            put("kddsn",kddsn)
            put("imei",imei)
        }
        val call : Call<Response> = retrofitService!!.registerDsn(body)
        call.enqueue(object: Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                listener.onRegisterDsnResult(response.body())
            }

        })
    }
}