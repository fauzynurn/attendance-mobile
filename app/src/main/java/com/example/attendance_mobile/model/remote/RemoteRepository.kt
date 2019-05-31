package com.example.attendance_mobile.model.remote

import android.os.Handler
import com.example.attendance_mobile.data.*
import com.example.attendance_mobile.detailmatkul.DetailMatkulContract
import com.example.attendance_mobile.home.homemhs.HomeMhsContract
import com.example.attendance_mobile.login.LoginContract
import com.example.fingerprintauth.RetrofitClient
import com.example.fingerprintauth.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback

class RemoteRepository {
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

    fun doFetchScheduleList(kelas: String, tgl : String, listener : HomeMhsContract.InteractorContract){
        val sd1 = Schedule("Pengolahan Citra Digital",
            true,
            "16TKO6022",
            "07:00:00",
            "08:40:00",
            "D219",
            "C2:00:E2:00:00:6A",
            "07:05:00",
            arrayListOf(Sesi(1,"07:00:00","07:50:00"),Sesi(2,"07:50:00","08:40:00")))
        val sd2 = Schedule("Pengantar Akuntansi",
            true,
            "16JTK6012",
            "08:40:00",
            "11:30:00",
            "D219",
            "C2:00:E2:00:00:6A",
            "08:45:00",
            arrayListOf(Sesi(1,"08:40:00","09:30:00"),Sesi(2,"09:30:00","10:20:00"),Sesi(3,"10:40:00","11:30:00")))
        val scheduleRes : ScheduleResponse = ScheduleResponse(arrayListOf(sd1,sd2),arrayListOf())
        Handler().postDelayed({
            listener.onScheduleListResult(scheduleRes)
        },2000)
//        val body : HashMap<String,String> = HashMap()
//        body["kdKelas"] = kelas
//        body["tgl"] = tgl
//        val call : Call<ArrayList<Schedule>> = retrofitService!!.fetchScheduleList(body)
//        call.enqueue(object: Callback<ScheduleResponse> {
//            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ScheduleResponse>, response: retrofit2.Response<ScheduleResponse>) {
//                listener.onScheduleListResult(response.body()!!)
//            }
//
//        })
    }

    fun doFetchDetailSummary(nim : String, listener : DetailMatkulContract.InteractorContract){
        val list = ArrayList<DetailSummary>()
        Handler().postDelayed({
            list.apply {
                add(DetailSummary(
                    "Pengolahan Citra Digital",
                    true,
                    12,
                    0
                ))
                add(DetailSummary(
                    "Proyek 5",
                    true,
                    30,
                    5
                ))
                add(DetailSummary(
                    "Pengantar Akuntansi",
                    true,
                    3,
                    0
                ))
                add(DetailSummary(
                    "Pengembangan Perangkat Lunak",
                    true,
                    9,
                    1
                ))
            }
            listener.onSummaryListResult(list)
        },4000)
//        val body : HashMap<String,String> = HashMap()
//        body["nim"] = nim
//        val call : Call<ArrayList<DetailSummary>> = retrofitService!!.fetchSummaryList(body)
//        call.enqueue(object: Callback<ArrayList<DetailSummary>> {
//            override fun onFailure(call: Call<ArrayList<DetailSummary>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ArrayList<DetailSummary>?>, response: retrofit2.Response<ArrayList<DetailSummary>>) {
//                listener.onSummaryListResult(response.body()!!)
//            }
//
//        })
    }


}