package com.example.fingerprintauth

import com.example.attendance_mobile.data.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitInterface {
    @POST("registermhs")
    fun registerMhs(@Body body : HashMap<String,String>) : Call<Response>

    @POST("registerdsn")
    fun registerDsn(@Body body : HashMap<String,String>) : Call<Response>

    @POST("checkmhs")
    fun checkMhs(@Body body : HashMap<String,String>) : Call<Response>

    @POST("checkdsn")
    fun checkDsn(@Body body : HashMap<String,String>) : Call<Response>

//    @POST("getPublicKey")
//    fun sendPublicKey(@Body pubKey : _PublicKey) : Call<Response>
}