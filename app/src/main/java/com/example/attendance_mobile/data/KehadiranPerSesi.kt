package com.example.attendance_mobile.data

import java.io.Serializable
data class KehadiranPerSesi(
    val sesi : Int,
    val jamMulai : String,
    val jamSelesai : String,
    val status : Boolean
) : Serializable
