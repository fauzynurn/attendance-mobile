package com.example.fingerprintauth

import com.example.attendance_mobile.data.DetailSummary
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.response.AttendanceResponse
import com.example.attendance_mobile.data.response.BaseResponse
import com.example.attendance_mobile.data.response.ScheduleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitInterface {
    @POST("registermhs")
    fun registerMhs(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("registerdsn")
    fun registerDsn(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("checkmhs")
    fun checkMhs(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("checkdsn")
    fun checkDsn(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("getpresencesummary")
    fun fetchSummary(@Body body : HashMap<String,String>) : Call<HashMap<String, Int>>

    @POST("getjadwal")
    fun fetchScheduleList(@Body body : HashMap<String,String>) : Call<ScheduleResponse<JadwalMhs>>

    @POST("getsummary")
    fun fetchSummaryList(@Body body : HashMap<String,String>) : Call<ArrayList<DetailSummary>>

    @POST("storeattendance")
    fun storeCurrentAttendance(@Body body : List<AttendanceResponse>) : Call<BaseResponse>
}