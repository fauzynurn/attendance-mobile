package com.example.attendance_mobile.data

import java.io.Serializable

data class DsnSchedule(
    val namaMatkul : String,
    val jenisMatkul : Boolean,
    val kodeMatkul : String,
    var jamMulai : String,
    val kelas : String,
    val jamMulaiOlehDosen : String,
    var jamSelesai : String,
    var ruangan: Ruangan
) : Serializable