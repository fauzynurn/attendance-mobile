package com.example.attendance_mobile.model.remote

import android.os.Handler
import com.example.attendance_mobile.data.Response
import com.example.attendance_mobile.data.ScheduleMhs
import com.example.attendance_mobile.home.homemhs.HomeMhsContract
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
    fun doValidateDosen(kddsn : String, pass : String, imei : String, listener : LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("kddsn",kddsn)
            put("password",pass)
            put("imei",imei)
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

    fun doValidateMhs(nim : String, pass : String, imei : String, listener: LoginContract.InteractorContract){
        val body : HashMap<String,String> = HashMap()
        body.run {
            put("nim",nim)
            put("password",pass)
            put("imei",imei)
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
                listener.onRegisterDsnResult()
            }

        })
    }

    fun doFetchSummary(nim: String, listener : HomeMhsContract.InteractorContract){
        val body : HashMap<String,Int> = HashMap()
        Handler().postDelayed({
            body.run{
                put("sakit",2)
                put("izin",4)
                put("alpa",0)
            }
            listener.onSummaryResult(body)
        },3000)
//        val call : Call<HashMap<String, Int>> = retrofitService!!.fetchPresenceSummary(body)
//        call.enqueue(object: Callback<HashMap<String,Int>> {
//            override fun onFailure(call: Call<HashMap<String,Int>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<HashMap<String,Int>>, response: retrofit2.Response<HashMap<String,Int>>) {
//
//            }
//
//        })
    }

    fun doFetchScheduleList(kelas: String, listener : HomeMhsContract.InteractorContract){
//        val body : HashMap<String,String> = HashMap()
        val list : ArrayList<ScheduleMhs> = ArrayList()
        Handler().postDelayed({
            list.add(ScheduleMhs(
                "Pengantar Akuntansi",
                1,
                "Arry Irawan",
                "08:40",
                "09:00",
                "D216",
                "NONE"
            ))
            list.add(ScheduleMhs(
                "Dasar-Dasar Pemograman",
                1,
                "Santi Sundari, Rahil Jumiyani",
                "09:00",
                "20:00",
                "D212",
                "NONE"
            ))
            list.add(ScheduleMhs(
                "Pengolahan Citra Digital",
                0,
                "Arry Irawan",
                "11:51",
                "12:00",
                "D216",
                "NONE"
            ))
            list.add(ScheduleMhs(
                "Pengantar Akuntansi",
                1,
                "Yudhi Widhisana",
                "13:44",
                "21:30",
                "D216",
                "C2:00:E2:00:00:6A"
            ))
//            body.run{
//                put("sakit",2)
//                put("izin",4)
//                put("alpa",0)
//            }

            listener.onScheduleListResult(list)
        },3000)
//        val call : Call<HashMap<String, Int>> = retrofitService!!.fetchPresenceSummary(body)
//        call.enqueue(object: Callback<HashMap<String,Int>> {
//            override fun onFailure(call: Call<HashMap<String,Int>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<HashMap<String,Int>>, response: retrofit2.Response<HashMap<String,Int>>) {
//
//            }
//
//        })
    }
}