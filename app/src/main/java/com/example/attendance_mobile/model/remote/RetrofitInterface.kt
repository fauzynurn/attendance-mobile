package com.example.fingerprintauth

import com.example.attendance_mobile.data.DetailAkumulasiKehadiran
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.request.JwlPenggantiRequest
import com.example.attendance_mobile.data.request.ListAttendanceRequest
import com.example.attendance_mobile.data.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT


interface RetrofitInterface {
    @POST("registermhs")
    fun registerMhs(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("registerdsn")
    fun registerDsn(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("checkmhs")
    fun checkMhs(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("checkdsn")
    fun checkDsn(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @POST("getketidakhadiran")
    fun fetchSummary(@Body body : HashMap<String,String>) : Call<HashMap<String, String>>

    @POST("getjadwalmhs")
    fun fetchScheduleMhsList(@Body body : HashMap<String,String>) : Call<ScheduleResponse<JadwalMhs>>

    @POST("getjadwaldsn")
    fun fetchScheduleDsnList(@Body body : HashMap<String,String>) : Call<ScheduleResponse<JadwalDsn>>

    @POST("getdaftarhadir")
    fun fetchMhsList(@Body body : HashMap<String,String>):Call<MhsListResponse>
    @POST("getkehadiran")
    fun fetchSummaryList(@Body body : HashMap<String,String>) : Call<ArrayList<DetailAkumulasiKehadiran>>

    @POST("mulaikbm")
    fun startClass(@Body body : HashMap<String,String>) : Call<BaseResponse>

    @PUT("catatkehadiran")
    fun storeCurrentAttendance(@Body body : ListAttendanceRequest) : Call<BaseResponse>

    @POST("dropdownmatkul")
    fun fetchMatkulList(@Body body : HashMap<String,String>) : Call<MatkulListResponse>

    @POST("dropdownjam")
    fun fetchAvailableTime(@Body body : HashMap<String,String>) : Call<TimeAvailableResponse>

    @POST("dropdownruangan")
    fun fetchAvailableRoom(@Body body : HashMap<String,String>) : Call<RoomAvailableResponse>

    @POST("buatjadwalpengganti")
    fun requestJwlPengganti(@Body jwlPengganti : JwlPenggantiRequest) : Call<BaseResponse>
}